package fordeckmacia

import scala.util.Try
import scodec.Codec
import scodec.codecs._

case class Card(set: Int, faction: Faction, cardNumber: Int) {
  require(cardNumber < 1000 && cardNumber >= 0, "Invalid card number")
  require(set < 100 && cardNumber >= 0, "Invalid set number")
  lazy val code: String = "%02d".format(set) + faction.id + "%03d".format(cardNumber)
}

object Card {

  def fromCode(code: String): Option[Card] =
    for {
      set     <- Try(code.take(2).toInt).toOption
      faction <- Faction.fromId(code.slice(2, 4))
      number  <- Try(code.slice(4, 7).toInt).toOption
    } yield Card(set, faction, number)

  private[fordeckmacia] def cardsOf1To3Codec: Codec[Set[Card]] =
    listOfN(vintL, factionSetCardsCodec).xmap(
      _.toSet.flatten,
      _.groupBy(card => (card.set, card.faction.int)).toList.sortBy(_._1).reverse.sortBy(_._2.size).map(_._2)
    )

  private[fordeckmacia] def cardsOf4PlusCodec: Codec[Map[Card, Int]] =
    list(vint ~ vintL ~ Faction.codec ~ vintL).xmap(
      _.map { case count ~ set ~ faction ~ cardNumber => Card(set, faction, cardNumber) -> count }.toMap,
      _.toList.sortBy(_._1.code).map { case (card, count) => count ~ card.set ~ card.faction ~ card.cardNumber }
    )

  private[this] val factionSetCardsCodec: Codec[Set[Card]] =
    vintL.consume { count =>
      (vintL ~ Faction.codec ~ listOfN(provide(count), vintL)).xmapc {
        case set ~ faction ~ cardNumbers =>
          cardNumbers.toSet[Int].map(cardNumber => Card(set, faction, cardNumber))
      }(cards => cards.head.set ~ cards.head.faction ~ cards.toList.sortBy(_.cardNumber).map(_.cardNumber))
    }(_.size)
}
