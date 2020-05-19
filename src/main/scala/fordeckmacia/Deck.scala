package fordeckmacia

case class Deck(cards: List[Card]) {
  require(cards.size <= 40 && cards.size > 0, "Invalid card count")
  def encode: String = Deck.encode(this)
}

object Deck {
  def decode(code: String): Option[Deck] = ???

  def encode(deck: Deck): String = ???

  val prefix = 0x11
}
