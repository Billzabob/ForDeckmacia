package fordeckmacia

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CardTest extends AnyFlatSpec with Matchers {
  "invalid cards" should "fail to be created" in {
    assertThrows[IllegalArgumentException](Card(100, Faction.Bilgewater, 1))
    assertThrows[IllegalArgumentException](Card(1, Faction.Bilgewater, 1000))
  }

  "invalid card codes" should "return None when parsed" in {
    Card.fromCode("") shouldBe None
    Card.fromCode("1") shouldBe None
    Card.fromCode("11N") shouldBe None
    Card.fromCode("ABCDEFG") shouldBe None
    Card.fromCode("11ZZ111") shouldBe None
    Card.fromCode("11NXABC") shouldBe None
  }
}
