package fordeckmacia

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CardTest extends AnyFlatSpec with Matchers {
  "invalid cards" should "fail to be created" in {
    assertThrows[IllegalArgumentException](Card(100, Faction.Bilgewater, 1))
    assertThrows[IllegalArgumentException](Card(1, Faction.Bilgewater, 1000))
  }
}
