package fordeckmacia

import cats.implicits._
import scodec.{Attempt, Decoder, Err}
import scodec.codecs.vintL
import scodec.interop.cats._

case class Card(set: Int, faction: Faction, cardNumber: Int) {
  require(cardNumber < 1000 && cardNumber >= 0, "Invalid card number")
  require(set < 100 && cardNumber >= 0, "Invalid set number")
  lazy val code: String = "%02d".format(set) + faction.id + "%03d".format(cardNumber)
}

object Card {
  def decoder(cardCount: Int): Decoder[List[Card]] =
    for {
      setFactionWith <- varIntDecoder
      cardsWith3     <- decodeFaction.replicateA(setFactionWith)
    } yield cardsWith3.flatten.flatMap(card => List.fill(cardCount)(card))

  private val varIntDecoder = vintL.asDecoder

  private val decodeFaction: Decoder[List[Card]] = for {
    count       <- varIntDecoder
    set         <- varIntDecoder
    factionInt  <- varIntDecoder
    faction     <- Decoder.liftAttempt(Attempt.fromOption(Faction.fromInt(factionInt), Err(s"Invalid faction number: $factionInt")))
    cardNumbers <- varIntDecoder.replicateA(count)
  } yield cardNumbers.map(cardNumber => Card(set, faction, cardNumber))
}
