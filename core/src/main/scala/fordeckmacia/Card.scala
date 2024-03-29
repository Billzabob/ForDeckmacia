package fordeckmacia

import scodec.Codec
import scodec.codecs.*

case class Card(set: Int, faction: Faction, cardNumber: Int):
  require(cardNumber < 1000 && cardNumber >= 0, s"Invalid card number: $cardNumber")
  require(set < 100 && set >= 0, s"Invalid set number: $set")
  lazy val code: String = "%02d".format(set) + faction.id + "%03d".format(cardNumber)

object Card:

  def fromCode(code: String): Option[Card] =
    for
      set     <- code.take(2).toIntOption
      faction <- Faction.fromId(code.slice(2, 4))
      number  <- code.slice(4, 7).toIntOption
    yield Card(set, faction, number)

  private[fordeckmacia] def cardsOf1To3Codec: Codec[Set[Card]] =
    listOfN(vint, factionSetCardsCodec).xmap(
      _.toSet.flatten,
      _.groupBy(card => (card.set, card.faction.int)).toList.sortBy(_._1).reverse.sortBy(_._2.size).map(_._2)
    )

  private[fordeckmacia] def cardsOf4PlusCodec: Codec[Map[Card, Int]] =
    list(vint :: vint :: Faction.codec :: vint).xmap(
      _.map { case (count, set, faction, cardNumber) => Card(set, faction, cardNumber) -> count }.toMap,
      _.toList.sortBy(_._1.code).map((card, count) => (count, card.set, card.faction, card.cardNumber))
    )

  private[this] val factionSetCardsCodec: Codec[Set[Card]] =
    vint.consume(count =>
      (vint :: Faction.codec :: listOfN(provide(count), vint)).xmap(
        (set, faction, cardNumbers) => cardNumbers.toSet.map(cardNumber => Card(set, faction, cardNumber)),
        cards => (cards.head.set, cards.head.faction, cards.toList.sortBy(_.cardNumber).map(_.cardNumber))
      )
    )(_.size)
