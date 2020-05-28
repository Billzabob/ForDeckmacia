package fordeckmacia

import cats.implicits._
import scodec.{Attempt, Codec, Err}
import scodec.bits.BitVector
import scodec.codecs._
import scodec.interop.cats._

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
    Attempt.fromOption(Base32.decode(code), Err("Not valid Base32")).flatMap(bytes => codec.complete.decodeValue(BitVector(bytes)))

  def encode(deck: Deck): Attempt[String] =
    codec.encode(deck).map(bits => Base32.encode(bits.toByteArray.toList))

  val supportedFormat     = 1
  val maxSupportedVersion = 2

  private val prefix = (supportedFormat << 4 | maxSupportedVersion).toByte

  val codec: Codec[Deck] = (byte ~ Card.codec ~ Card.codec ~ Card.codec).narrowc {
    case prefix ~ cardsOf3 ~ cardsOf2 ~ cardsOf1 =>
      Attempt.guard(checkVersion(prefix), unsupportedVersion(prefix)).as(Deck(duplicate(cardsOf3, 3) ::: duplicate(cardsOf2, 2) ::: cardsOf1))
  }(deck => prefix ~ cardsOf(deck.cards, 3)(_.code) ~ cardsOf(deck.cards, 2)(_.code) ~ cardsOf(deck.cards, 1)(_.code))

  def duplicate[A](list: List[A], count: Int): List[A] = list.flatMap(a => List.fill(count)(a))

  def cardsOf[A, B: cats.Order](list: List[A], count: Int)(f: A => B) =
    list.groupByNel(f).values.filter(_.size == count).toList.map(_.head)

  private def checkVersion(byte: Byte): Boolean = {
    val format  = (byte & 0xf0) >> 4
    val version = byte & 0x0f
    (format == supportedFormat && version <= maxSupportedVersion)
  }

  private def unsupportedVersion(version: Byte) =
    s"Unsupported deckcode version or format: $version. Please update ForDeckmacia or create an Issue/PR if there isn't a newer version"
}
