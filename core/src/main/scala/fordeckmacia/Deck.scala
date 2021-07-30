package fordeckmacia

import scodec.bits.BitVector
import scodec.codecs.*
import scodec.*

case class Deck(cards: Map[Card, Int]):

  def encode: Attempt[String] = Deck.encode(this)

  def cardList: List[Card]    = cards.toList.flatMap((card, count) => List.fill(count)(card))
  def codeList: List[String]  = cardList.map(_.code)
  def codes: Map[String, Int] = cards.map((card, count) => (card.code, count))

object Deck:
  def decode(code: String): Attempt[Deck] =
    Attempt.fromEither(BitVector.fromBase32Descriptive(code.toUpperCase).left.map(Err.apply)).flatMap(codec.complete.decodeValue)

  def encode(deck: Deck): Attempt[String] =
    codec.encode(deck).map(_.toBase32.takeWhile(_ != '='))

  def fromCards(cards: List[Card]): Deck =
    Deck(cards.groupBy(card => card).map((card, cards) => (card, cards.size)))

  val supportedFormat     = 1.toByte
  val maxSupportedVersion = 4.toByte

  def codec: Codec[Deck] =
    (prefixCodec :: Card.cardsOf1To3Codec :: Card.cardsOf1To3Codec :: Card.cardsOf1To3Codec :: Card.cardsOf4PlusCodec).xmap(
      (_, cardsOf3, cardsOf2, cardsOf1, cardsOf4Plus) =>
        def toMap[A](set: Set[A], count: Int): Map[A, Int] = set.map(_ -> count).toMap
        Deck(toMap(cardsOf3, 3) ++ toMap(cardsOf2, 2) ++ toMap(cardsOf1, 1) ++ cardsOf4Plus),
      deck =>
        def cardsOfCount[A](map: Map[A, Int], count: Int): Set[A] = map.filter(_._2 == count).keySet
        def cardsOf4Plus[A](map: Map[A, Int]): Map[A, Int]        = map.filter(_._2 >= 4)
        (calculateVersion(deck), cardsOfCount(deck.cards, 3), cardsOfCount(deck.cards, 2), cardsOfCount(deck.cards, 1), cardsOf4Plus(deck.cards))
    )

  private[this] val prefixCodec: Codec[Byte] = (ubyte(4) :: ubyte(4)).narrow(
    (format, version) => Attempt.guard(format == supportedFormat && version <= maxSupportedVersion, Err(unsupportedVersion(version))).map(_ => version),
    version => (supportedFormat, version)
  )

  private[this] def calculateVersion(deck: Deck): Byte =
    deck.cards.keys.map(_.faction).map(versionsForFactions).maxOption.getOrElse(1.toByte)

  private[this] val versionsForFactions: Faction => Byte =
    case Faction.Demacia         => 1
    case Faction.Freljord        => 1
    case Faction.Ionia           => 1
    case Faction.Noxus           => 1
    case Faction.PiltoverAndZaun => 1
    case Faction.ShadowIsles     => 1
    case Faction.Bilgewater      => 2
    case Faction.MountTargon     => 2
    case Faction.Shurima         => 3
    case Faction.BandleCity      => 4

  private[this] def unsupportedVersion(version: Byte) =
    s"Unsupported deckcode version or format: $version. Please update ForDeckmacia or create an Issue/PR if there isn't a newer version"
