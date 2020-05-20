package fordeckmacia

case class Card(set: Int, faction: Faction, cardNumber: Int) {
  require(cardNumber < 1000 && cardNumber >= 0, "Invalid card number")
  lazy val code: String = "%02d".format(set) + faction.id + "%03d".format(cardNumber)
}
