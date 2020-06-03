package fordeckmacia

import scodec.{Attempt, Codec}
import scodec.codecs._

case class Deck(cards: Map[Card, Int]) {

  def encode: Attempt[String] = Deck.encode(this)

  def cardList: List[Card]    = cards.toList.flatMap { case (card, count) => List.fill(count)(card) }
  def codeList: List[String]  = cardList.map(_.code)
  def codes: Map[String, Int] = cards.map { case (card, count) => (card.code, count) }

  def cardCountsValid: Boolean = cards.values.forall((0 to 3).contains)
}

object Deck {
  def decode(code: String): Attempt[Deck] =
    Base32.decode(code).map(_.bits).flatMap(codec.complete.decodeValue)

  def encode(deck: Deck): Attempt[String] =
    codec.encode(deck).map(Base32.encode)

  def fromCards(cards: List[Card]): Deck =
    Deck(cards.groupBy(card => card).map { case (card, cards) => (card, cards.size) })

  val supportedFormat     = 1
  val maxSupportedVersion = 2

  private val prefix = (supportedFormat << 4 | maxSupportedVersion).toByte

  val codec: Codec[Deck] = (byte ~ Card.codec ~ Card.codec ~ Card.codec).exmapc {
    case prefix ~ cardsOf3 ~ cardsOf2 ~ cardsOf1 =>
      def toMap[A](set: Set[A], count: Int) = set.map(_ -> count).toMap
      Attempt.guard(checkVersion(prefix), unsupportedVersion(prefix)).map(_ => Deck(toMap(cardsOf3, 3) ++ toMap(cardsOf2, 2) ++ toMap(cardsOf1, 1)))
  } { deck =>
    def cardsOfCount[A](map: Map[A, Int], count: Int) = map.filter(_._2 == count).keySet
    Attempt
      .guard(deck.cardCountsValid, "Can't encode deck with invalid card counts")
      .map(_ => prefix ~ cardsOfCount(deck.cards, 3) ~ cardsOfCount(deck.cards, 2) ~ cardsOfCount(deck.cards, 1))
  }

  private def checkVersion(byte: Byte): Boolean = {
    val format  = (byte & 0xf0) >> 4
    val version = byte & 0x0f
    (format == supportedFormat && version <= maxSupportedVersion)
  }

  private def unsupportedVersion(version: Byte) =
    s"Unsupported deckcode version or format: $version. Please update ForDeckmacia or create an Issue/PR if there isn't a newer version"
}
