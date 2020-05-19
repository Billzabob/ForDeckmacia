package fordeckmacia

sealed trait Faction extends Product with Serializable { def id: String }

object Faction {
  case object Demacia         extends Faction { val id = "DE" }
  case object Freljord        extends Faction { val id = "FR" }
  case object Ionia           extends Faction { val id = "IO" }
  case object Noxus           extends Faction { val id = "NX" }
  case object PiltoverAndZaun extends Faction { val id = "PZ" }
  case object ShadowIsles     extends Faction { val id = "SI" }
  case object Bilgewater      extends Faction { val id = "BW" }
}
