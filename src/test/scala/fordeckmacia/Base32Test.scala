package fordeckmacia

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class Base32Test extends AnyFlatSpec with Matchers with ScalaCheckDrivenPropertyChecks {

  implicit val config = PropertyCheckConfiguration(minSuccessful = 10000)

  "base32" should "be idempotent" in {
    forAll { bytes: List[Byte] =>
      val encoded   = Base32.encode(bytes)
      val decoded   = Base32.decode(encoded)
      val reEncoded = decoded.map(Base32.encode)
      decoded shouldBe Some(bytes)
      reEncoded shouldBe Some(encoded)
    }
  }

  it should "return None if decoding invalid Base32 value" in {
    Base32.decode("01") shouldBe None
  }
}
