package fordeckmacia

import org.scalacheck.Gen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import scala.io.Source
import scala.util.Random
import scodec.Attempt

class DeckTest extends AnyFlatSpec with Matchers with ScalaCheckDrivenPropertyChecks {

  implicit val config = PropertyCheckConfiguration(minSuccessful = 10000)

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

  "deck encoding/decoding" should "be idempotent" in {
    forAll(deckGenerator) { deck: Deck =>
      val encoded   = deck.encode
      val decoded   = encoded.flatMap(Deck.decode)
      val reEncoded = decoded.flatMap(_.encode)
      decoded shouldBe Attempt.successful(deck)
      reEncoded shouldBe encoded
    }
  }

  it should "not depend on card order" in {
    forAll(deckGenerator) { deck: Deck =>
      val encoded          = deck.encode
      val encodedScrambled = Deck.fromCards(Random.shuffle(deck.cardList)).encode
      encodedScrambled shouldBe encoded
    }
  }

  it should "fail on unexpected version" in {
    Deck.decode("D4AAAAA") should matchPattern { case Attempt.Failure(_) => }
  }

  it should "fail on invalid deck codes" in {
    Deck.decode("CIAAAAI") should matchPattern { case Attempt.Failure(_) => }
  }

  it should "fail on unknown factions" in {
    Deck.decode("CIAAAAIBAB7QA") should matchPattern { case Attempt.Failure(_) => }
  }

  it should "fail on invalid Base32" in {
    Deck.decode("CIAAAAA01") should matchPattern { case Attempt.Failure(_) => }
  }

  it should "work for decks" in {
    verifyDeck("deck1.txt")
    verifyDeck("deck2.txt")
    verifyDeck("deckWith4PlusDuplicates.txt")
  }

  it should "generate the same codes as the game" in {
    def verifyCode(code: String) = Deck.decode(code).flatMap(_.encode) shouldBe Attempt.successful(code)
    // These codes were exported directly from the game
    verifyCode("CIBQCAQFBIBACBI5FACQEBQ5E4XTKOADAIBAMJJMAIBAKBYIAIAQKGJWAMAQEBRWAEBAKAYCAECSWNA")
    verifyCode("CICACAQEBABAEAQBBECACAQCBQTDSBIBAQIBWJZUHAAQEAICEUYQA")
    verifyCode("CIAAAAH7777X6AIFGU")
  }

  "deck to and from card list" should "be idempotent" in {
    forAll(deckGenerator) { deck: Deck =>
      val encoded   = deck.codeList.sorted
      val decoded   = Deck.fromCards(encoded.flatMap(Card.fromCode))
      val reEncoded = decoded.codeList.sorted
      decoded shouldBe deck
      reEncoded shouldBe encoded
    }
  }

  "deck to and from card codes" should "be idempotent" in {
    forAll(deckGenerator) { deck: Deck =>
      val encoded   = deck.codes
      val decoded   = Deck.fromCards(encoded.toList.flatMap { case (code, count) => List.fill(count)(code) }.flatMap(Card.fromCode))
      val reEncoded = decoded.codes
      decoded shouldBe deck
      reEncoded shouldBe encoded
    }
  }

  private def verifyDeck(resource: String) = {
    val deck = readDeckFromResource(resource)
    Deck.decode(deck.code).map(_.codeList.sorted) shouldBe Attempt.successful(deck.cardCodes.sorted)
    Deck.fromCards(deck.cardCodes.flatMap(Card.fromCode)).encode shouldBe Attempt.successful(deck.code)
  }

  case class DeckAndCode(code: String, cardCodes: List[String])

  private def readDeckFromResource(resource: String): DeckAndCode = {
    val lines = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(resource)).getLines.toList
    DeckAndCode(lines.head, lines.tail.filterNot(_.isEmpty))
  }
}
