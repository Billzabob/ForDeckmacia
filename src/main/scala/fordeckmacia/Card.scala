package fordeckmacia

import scala.util.Try
import scodec.{Attempt, Codec, Err}
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
      faction <- Faction.fromId(code.drop(2).take(2))
      number  <- Try(code.drop(4).take(3).toInt).toOption
    } yield Card(set, faction, number)

  def codec: Codec[List[Card]] =
    listOfN(vintL, factionCodec).xmapc(_.flatMap(_.toList)) { cards =>
      cards.groupBy(card => (card.set, card.faction.int)).values.toList.sortBy(_.head.cardNumber).sortBy(_.size)
    }

  private val factionCodec: Codec[List[Card]] =
    vintL.consume { count =>
      (vintL ~ vintL ~ listOfN(provide(count), vintL)).narrowc {
        case set ~ factionInt ~ cardNumbers =>
          val faction = Attempt.fromOption(Faction.fromInt(factionInt), Err(s"Invalid faction number $factionInt"))
          faction.map(faction => cardNumbers.map(cardNumber => Card(set, faction, cardNumber)))
      }(cards => cards.head.set ~ cards.head.faction.int ~ cards.sortBy(_.cardNumber).map(_.cardNumber))
    }(_.size)
}
