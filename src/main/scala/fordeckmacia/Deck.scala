package fordeckmacia
import scala.collection.immutable.Nil

case class Deck(cards: List[Card]) {
  def encode: String = Deck.encode(this)
}

object Deck {
  def decode(code: String): Option[Deck] =
    for {
      bytes     <- Base32.decode(code)
      firstByte <- bytes.headOption
      rest      <- if (firstByte == prefix) Some(bytes.tail) else None
      result    <- decodeInts(VarInt.fromBytes(rest).map(_.toInt))
    } yield result

  private def decodeInts(values: List[Int]): Option[Deck] = loopCardCounts(values, 3)

  def encode(deck: Deck): String = {
    val counts: Map[Card, Int] = deck.cards.groupBy(card => card).map { case (card, cards) => (card, cards.size) }

    val cardsOf3 = orderCards(counts, 3)
    val cardsOf2 = orderCards(counts, 2)
    val cardsOf1 = orderCards(counts, 1)

    Base32.encode(prefix :: encodeOrderedValues(cardsOf3) ::: encodeOrderedValues(cardsOf2) ::: encodeOrderedValues(cardsOf1))
  }

  private def loopFactions(remaining: List[Int], setFactionCountLeft: Int, cards: Option[List[Card]] = Some(Nil)): Option[(List[Card], List[Int])] = {
    remaining match {
      case _ if cards.isEmpty            => None
      case _ if setFactionCountLeft == 0 => cards.map(cards => (cards, remaining))
      case setFactions :: next =>
        val (ours, rest) = next.splitAt(setFactions + 2)
        val newCards = for {
          set        <- ours.lift(0)
          factionInt <- ours.lift(1)
          faction    <- Faction.fromInt(factionInt)
        } yield ours.drop(2).take(setFactions).map(cardId => Card(set, faction, cardId))
        loopFactions(rest, setFactionCountLeft - 1, cards.flatMap(cards => newCards.map(_ ::: cards)))
      case Nil => None
    }
  }

  private def loopCardCounts(values: List[Int], cardCount: Int, deck: Option[Deck] = Some(Deck(Nil))): Option[Deck] =
    if (cardCount == 0) deck
    else {
      for {
        factionCount       <- values.headOption
        (cards, remaining) <- loopFactions(values.drop(1), factionCount)
        deck               <- loopCardCounts(remaining, cardCount - 1, deck.map(deck => Deck(deck.cards ::: cards.flatMap(card => List.fill(cardCount)(card)))))
      } yield deck
    }

  private def orderCards(cardCounts: Map[Card, Int], count: Int): List[List[Card]] = {
    cardCounts
      .filter { case (_, c) => c == count }
      .keySet
      .groupBy(card => (card.set, card.faction))
      .values
      .toList
      .sortBy(_.size)
      .map(_.toList.sortBy(_.code))
  }

  private def encodeOrderedValues(orderedCards: List[List[Card]]): List[Byte] = {
    val values = orderedCards.size :: orderedCards.flatMap { factionCards =>
      factionCards.size :: factionCards.head.set :: factionCards.head.faction.int :: factionCards.map(_.cardNumber)
    }

    values.map(VarInt.fromInt).flatMap(_.bytes)
  }

  val prefix = 0x11.toByte
}
