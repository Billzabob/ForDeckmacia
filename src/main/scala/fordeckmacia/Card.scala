package fordeckmacia

case class Card(set: CardSet, faction: Faction, cardNumber: Int) {
  require(cardNumber < 1000 && cardNumber >= 0, "Invalid card number")
  lazy val code: String = set.id + faction.id + "%03d".format(cardNumber)
}
