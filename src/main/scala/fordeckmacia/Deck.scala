package fordeckmacia

case class Deck(cards: List[Card]) {
  def encode: String = Deck.encode(this)
}

object Deck {
  def decode(code: String): Option[Deck] = ???

  def encode(deck: Deck): String = {
    val counts: Map[Card, Int] = deck.cards.groupBy(card => card).map { case (card, cards) => (card, cards.size) }

    val cardsOf3 = orderCards(counts, 3)
    val cardsOf2 = orderCards(counts, 2)
    val cardsOf1 = orderCards(counts, 1)

    Base32.encode(prefix :: encodeOrderedValues(cardsOf3) ::: encodeOrderedValues(cardsOf2) ::: encodeOrderedValues(cardsOf1))
  }

  def orderCards(cardCounts: Map[Card, Int], count: Int): List[List[Card]] = {
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
