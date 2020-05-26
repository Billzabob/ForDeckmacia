package fordeckmacia

import org.scalacheck.Gen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class DeckTest extends AnyFlatSpec with Matchers with ScalaCheckDrivenPropertyChecks {

  val cardGenerator: Gen[Card] =
    for {
      set     <- Gen.choose(1, 99)
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
      cardStream <- Gen.listOfN(40, cardsGenerator)
      numCards   <- Gen.choose(0, 40) // Can have 0 to 40 cards in a deck
      cards       = cardStream.flatten.take(numCards).toList
    } yield Deck(cards)

  "deck encoding/decoding" should "be idempotent" in {
    forAll(deckGenerator) { deck: Deck =>
      val encoded   = deck.encode
      val decoded   = Deck.decode(encoded)
      val reEncoded = decoded.map(_.encode)
      decoded shouldBe Some(deck)
      reEncoded shouldBe Some(encoded)
    }
  }

  it should "fail on unexpected version" in {
    assertThrows[RuntimeException](Deck.decode("D4AAAAA"))
  }

  it should "return None on invalid deck codes" in {
    Deck.decode("CIAAAAI")
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
}
