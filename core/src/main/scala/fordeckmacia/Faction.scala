package fordeckmacia

import scodec.codecs.vint
import scodec.{Attempt, Codec, Err}

sealed trait Faction extends Product with Serializable {
  def id: String
  def int: Int
}

object Faction {
  case object Demacia         extends Faction { val id = "DE"; val int = 0 }
  case object Freljord        extends Faction { val id = "FR"; val int = 1 }
  case object Ionia           extends Faction { val id = "IO"; val int = 2 }
  case object Noxus           extends Faction { val id = "NX"; val int = 3 }
  case object PiltoverAndZaun extends Faction { val id = "PZ"; val int = 4 }
  case object ShadowIsles     extends Faction { val id = "SI"; val int = 5 }
  case object Bilgewater      extends Faction { val id = "BW"; val int = 6 }
  case object MountTargon     extends Faction { val id = "MT"; val int = 9 }

  val allFactions = List(Demacia, Freljord, Ionia, Noxus, PiltoverAndZaun, ShadowIsles, Bilgewater, MountTargon)

  def fromInt(int: Int): Option[Faction]  = allFactions.find(_.int == int)
  def fromId(id: String): Option[Faction] = allFactions.find(_.id == id)

  private[fordeckmacia] val codec: Codec[Faction] =
    vint.narrow(factionInt => Attempt.fromOption(Faction.fromInt(factionInt), Err(s"Invalid faction number $factionInt")), _.int)
}
