package fordeckmacia

import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Prop._
import scodec.Attempt

class DeckTest extends ScalaCheckSuite {

  override def scalaCheckTestParameters =
    super.scalaCheckTestParameters
      .withMinSuccessfulTests(10000)

  val cardGenerator: Gen[Card] =
    for {
      set     <- Gen.choose(1, 2)
      faction <- Gen.oneOf(Faction.allFactions)
      cardNum <- Gen.choose(0, 999)
    } yield Card(set, faction, cardNum)

  val cardsGenerator: Gen[(Card, Int)] =
    for {
      numCards <- Gen.frequency(50 -> Gen.choose(1, 3), 1 -> Gen.chooseNum(4, 10))
      card     <- cardGenerator
    } yield (card, numCards)

  val deckGenerator: Gen[Deck] =
    for {
      numDifferentCards <- Gen.choose(0, 100)
      cards             <- Gen.listOfN(numDifferentCards, cardsGenerator)
    } yield Deck(cards.toMap)

  property("idempotent deck encoding/decoding") {
    forAll(deckGenerator) { deck: Deck =>
      val encoded   = deck.encode
      val decoded   = encoded.flatMap(Deck.decode)
      val reEncoded = decoded.flatMap(_.encode)
      assertEquals(decoded, Attempt.successful(deck))
      assertEquals(reEncoded, encoded)
    }
  }

  test("fail on unexpected version") {
    assert(clue(Deck.decode("D4AAAAA")).isFailure)
  }

  test("fail on invalid deck codes") {
    assert(clue(Deck.decode("CIAAAAI")).isFailure)
  }

  test("fail on unknown factions") {
    assert(clue(Deck.decode("CIAAAAIBAB7QA")).isFailure)
  }

  test("fail on invalid Base32") {
    assert(clue(Deck.decode("CIAAAAA01")).isFailure)
  }

  test("work for lowercase deck codes") {
    assert(clue(Deck.decode("cibqcaqfbibacbi5facqebq5e4xtkoadaibamjjmaibakbyiaiaqkgjwamaqebrwaebakaycaecswna")).isSuccessful)
  }

  test("work for decks") {
    verifyDeck(deck1)
    verifyDeck(deck2)
    verifyDeck(deck3)
  }

  test("generate the same codes as the game") {
    def verifyCode(code: String) = assertEquals(Deck.decode(code).flatMap(_.encode), Attempt.successful(code))
    // These codes were exported directly from the game
    verifyCode("CIBQCAQFBIBACBI5FACQEBQ5E4XTKOADAIBAMJJMAIBAKBYIAIAQKGJWAMAQEBRWAEBAKAYCAECSWNA")
    verifyCode("CICACAQEBABAEAQBBECACAQCBQTDSBIBAQIBWJZUHAAQEAICEUYQA")
    verifyCode("CIAAAAH7777X6AIFGU")
  }

  test("idempotent deck to and from card list") {
    forAll(deckGenerator) { deck: Deck =>
      val encoded   = deck.codeList.sorted
      val decoded   = Deck.fromCards(encoded.flatMap(Card.fromCode))
      val reEncoded = decoded.codeList.sorted
      assertEquals(decoded, deck)
      assertEquals(reEncoded, encoded)
    }
  }

  test("idempotent deck to and from card codes") {
    forAll(deckGenerator) { deck: Deck =>
      val encoded   = deck.codes
      val decoded   = Deck.fromCards(encoded.toList.flatMap { case (code, count) => List.fill(count)(code) }.flatMap(Card.fromCode))
      val reEncoded = decoded.codes
      assertEquals(decoded, deck)
      assertEquals(reEncoded, encoded)
    }
  }

  def verifyDeck(deckString: String) = {
    val deck = parseDeck(deckString)
    assertEquals(Deck.decode(deck.code).map(_.codeList.sorted), Attempt.successful(deck.cardCodes.sorted))
    assertEquals(Deck.fromCards(deck.cardCodes.flatMap(Card.fromCode)).encode, Attempt.successful(deck.code))
  }

  case class DeckAndCode(code: String, cardCodes: List[String])

  def parseDeck(deck: String): DeckAndCode = {
    val lines = deck.linesIterator.toList.map(_.trim)
    DeckAndCode(lines.head, lines.tail.filterNot(_.isEmpty))
  }

  val deck1 =
    """CIBQEAQDAMCAIAIEBAITINQGAEBQEDY6EUUC6AICAECB6JYA
    01PZ008
    01PZ008
    01PZ008
    01NX040
    01NX040
    01NX040
    01NX015
    01NX015
    01NX015
    01NX037
    01NX037
    01NX037
    01NX030
    01NX030
    01NX030
    01PZ054
    01PZ054
    01PZ054
    02NX004
    02NX004
    02NX004
    01PZ017
    01PZ017
    01PZ017
    01NX047
    01NX047
    01NX047
    01PZ052
    01PZ052
    01PZ052
    01PZ039
    01PZ039
    02NX003
    02NX003
    02NX003
    01PZ031
    01PZ031
    01NX002
    01NX002
    01NX002"""

  val deck2 =
    """CIBQCAQEBABACBA3GQCQCBI5EEUDKNQDAEBAIBQCAECB6MAEAECQCKZRHAAQEAIFB4MQ
    01SI053
    01SI053
    01SI053
    02PZ008
    02PZ008
    02PZ008
    01SI043
    01SI043
    01SI056
    01SI056
    01PZ048
    01PZ048
    01SI033
    01SI033
    01SI033
    01PZ027
    01PZ027
    01PZ027
    01SI049
    01SI049
    01SI040
    01SI040
    01SI040
    01PZ052
    01PZ052
    01PZ052
    01PZ031
    01PZ031
    02PZ006
    02PZ006
    01SI054
    01SI054
    01SI054
    01SI029
    01SI029
    01SI029
    01SI025
    01SI001
    01SI001
    01SI015"""

  val deck3 =
    """CIAAAAAKAECTK
    01SI053
    01SI053
    01SI053
    01SI053
    01SI053
    01SI053
    01SI053
    01SI053
    01SI053
    01SI053"""
}
