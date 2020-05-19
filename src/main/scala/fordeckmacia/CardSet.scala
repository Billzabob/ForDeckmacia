package fordeckmacia

sealed trait CardSet extends Product with Serializable { def id: String }

object CardSet {
  case object Set1 extends CardSet { val id = "01" }
  case object Set2 extends CardSet { val id = "02" }
}
