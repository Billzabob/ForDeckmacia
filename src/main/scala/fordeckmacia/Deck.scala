package fordeckmacia

import scodec.{Attempt, Codec}
import scodec.codecs._

case class Deck(cards: List[Card]) {

  def encode: Attempt[String] = Deck.encode(this)

  override def equals(a: Any) =
    a match {
      case d: Deck => encode == d.encode
      case _       => false
    }

  override def hashCode = encode.hashCode
}

object Deck {
  def decode(code: String): Attempt[Deck] =
    Base32.decode(code).map(_.bits).flatMap(codec.complete.decodeValue)

  def encode(deck: Deck): Attempt[String] =
    codec.encode(deck).map(Base32.encode)

  val supportedFormat     = 1
  val maxSupportedVersion = 2

  private val prefix = (supportedFormat << 4 | maxSupportedVersion).toByte

  val codec: Codec[Deck] = (byte ~ Card.codec ~ Card.codec ~ Card.codec).narrowc {
    case prefix ~ cardsOf3 ~ cardsOf2 ~ cardsOf1 =>
      Attempt.guard(checkVersion(prefix), unsupportedVersion(prefix)).map(_ => Deck(duplicate(cardsOf3, 3) ::: duplicate(cardsOf2, 2) ::: cardsOf1))
  }(deck => prefix ~ cardsOf(deck.cards, 3)(_.code) ~ cardsOf(deck.cards, 2)(_.code) ~ cardsOf(deck.cards, 1)(_.code))

  private def duplicate[A](list: List[A], count: Int): List[A] = list.flatMap(a => List.fill(count)(a))

  private def cardsOf[A, B](list: List[A], count: Int)(f: A => B): List[A] =
    list.groupBy(f).values.filter(_.size == count).toList.map(_.head)

  private def checkVersion(byte: Byte): Boolean = {
    val format  = (byte & 0xf0) >> 4
    val version = byte & 0x0f
    (format == supportedFormat && version <= maxSupportedVersion)
  }

  private def unsupportedVersion(version: Byte) =
    s"Unsupported deckcode version or format: $version. Please update ForDeckmacia or create an Issue/PR if there isn't a newer version"
}
