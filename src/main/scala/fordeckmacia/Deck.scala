package fordeckmacia

import cats.implicits._
import scodec.{Attempt, Decoder, Encoder, Err}
import scodec.bits.BitVector
import scodec.codecs.{byte, vintL}
import scodec.interop.cats._
import scodec.SizeBound

case class Deck(cards: List[Card]) {

  def encode: Attempt[String] = Deck.encode(this)

  override def equals(a: Any) =
    a match {
      case Deck(otherCards) => cards.sortBy(_.code) == otherCards.sortBy(_.code)
      case _                => false
    }

  override def hashCode = cards.sortBy(_.code).hashCode
}

object Deck {
  def decode(code: String): Attempt[Deck] =
    Attempt.fromOption(Base32.decode(code), Err("Not valid Base32")).flatMap(bytes => decoder.complete.decodeValue(BitVector(bytes)))

  def encode(deck: Deck): Attempt[String] =
    encoder.encode(deck).map(bits => Base32.encode(bits.toByteArray.toList))

  val supportedFormat     = 1
  val maxSupportedVersion = 2

  private val prefix = (supportedFormat << 4 | maxSupportedVersion).toByte

  private case class SetFactionPair(set: Int, faction: Faction)

  private val encoder: Encoder[Deck] =
    new Encoder[Deck] {
      def encode(deck: Deck) =
        for {
          prefix   <- byte.encode(prefix)
          cardsOf3 <- encodeCardCount(deck, 3)
          cardsOf2 <- encodeCardCount(deck, 2)
          cardsOf1 <- encodeCardCount(deck, 1)
        } yield prefix ++ cardsOf3 ++ cardsOf2 ++ cardsOf1

      val sizeBound = SizeBound.atLeast(32)

      private def encodeCardCount(deck: Deck, cardCount: Int) = {
        val cardsOfN = deck.cards.groupBy(card => card).filter(_._2.size == cardCount).keySet.groupBy(card => SetFactionPair(card.set, card.faction)).toList
        for {
          cardCount <- vintL.encode(cardsOfN.size)
          cards     <- cardsOfN.sortBy(_._2.size).foldMapM { case (setFaction, cards) => encodeSetFaction(setFaction, cards) }
        } yield cardCount ++ cards
      }

      private def encodeSetFaction(setFaction: SetFactionPair, cards: Set[Card]) =
        for {
          count   <- vintL.encode(cards.size)
          set     <- vintL.encode(setFaction.set)
          faction <- vintL.encode(setFaction.faction.int)
          cards   <- cards.toList.map(_.cardNumber).sorted.foldMapM(vintL.encode)
        } yield count ++ set ++ faction ++ cards
    }

  private val decoder: Decoder[Deck] = for {
    version  <- byte
    _        <- Decoder.liftAttempt(Attempt.guard(checkVersion(version), Err(unsupportedVersion(version))))
    cardsOf3 <- Card.decoder(3)
    cardsOf2 <- Card.decoder(2)
    cardsOf1 <- Card.decoder(1)
  } yield Deck(cardsOf3 ::: cardsOf2 ::: cardsOf1)

  private def checkVersion(byte: Byte): Boolean = {
    val format  = (byte & 0xf0) >> 4
    val version = byte & 0x0f
    (format == supportedFormat && version <= maxSupportedVersion)
  }

  private def unsupportedVersion(version: Byte) =
    s"Unsupported deckcode version or format: $version. Please update ForDeckmacia or create an Issue/PR if there isn't a newer version"
}
