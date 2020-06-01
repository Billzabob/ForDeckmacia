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

  val cardsGenerator: Gen[List[Card]] =
    for {
      numCards <- Gen.choose(1, 3) // Can have between 1 and 3 of a card
      card     <- cardGenerator
    } yield List.fill(numCards)(card)

  val deckGenerator: Gen[Deck] =
    for {
      cardStream       <- Gen.listOfN(40, cardsGenerator)
      uniqueCardStream <- if (cardStream.flatten.groupBy(card => card).exists(_._2.size > 3)) Gen.fail else Gen.const(cardStream) // Very rare case to gen the same card twice
      numCards         <- Gen.choose(0, 40) // Can have 0 to 40 cards in a deck
      cards             = uniqueCardStream.flatten.take(numCards).toList
    } yield Deck(cards)

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
      val encodedScrambled = Deck(Random.shuffle(deck.cards)).encode
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

  it should "work for deck1" in {
    verifyDeck("deck1.txt")
  }

  it should "work for deck2" in {
    verifyDeck("deck2.txt")
  }

  "decks" should "be equal if order is different" in {
    val card1 = Card(1, Faction.Bilgewater, 1)
    val card2 = Card(1, Faction.Demacia, 1)
    val deck1 = Deck(List(card1, card2))
    val deck2 = Deck(List(card2, card1))

    deck1 shouldBe deck2
    deck1.hashCode shouldBe deck2.hashCode
    deck1 should not be card1
  }

  private def verifyDeck(resource: String) = {
    val deck = readDeckFromResource(resource)
    Deck.decode(deck.code).map(_.cards.map(_.code).sorted) shouldBe Attempt.successful(deck.cardCodes.sorted)
    Deck(deck.cardCodes.flatMap(Card.fromCode)).encode shouldBe Attempt.successful(deck.code)
  }

  case class DeckAndCode(code: String, cardCodes: List[String])

  private def readDeckFromResource(resource: String): DeckAndCode = {
    val lines = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(resource)).getLines.toList
    DeckAndCode(lines.head, lines.tail.filterNot(_.isEmpty))
  }
}
