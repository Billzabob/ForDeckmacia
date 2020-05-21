package fordeckmacia

import org.scalacheck.Gen
import org.scalacheck.Properties
import org.scalacheck.Prop._

object DeckTest extends Properties("Deck") {

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

  property("idempotent") = forAll(deckGenerator) { deck: Deck =>
    val encoded   = Deck.encode(deck)
    val decoded   = Deck.decode(encoded)
    val reEncoded = decoded.map(Deck.encode)
    decoded == Some(deck) && reEncoded == Some(encoded)
  }
}
