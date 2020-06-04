package fordeckmacia

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import scodec.Attempt
import scodec.bits.BitVector

class Base32Test extends AnyFlatSpec with Matchers with ScalaCheckDrivenPropertyChecks {

  implicit val config = PropertyCheckConfiguration(minSuccessful = 10000)

  "base32" should "be idempotent" in {
    forAll { bits: Array[Byte] =>
      val bitVector = BitVector(bits)
      val encoded   = Base32.encode(bitVector)
      val decoded   = Base32.decode(encoded).map(_.bits)
      val reEncoded = decoded.map(Base32.encode)
      decoded shouldBe Attempt.successful(bitVector)
      reEncoded shouldBe Attempt.successful(encoded)
    }
  }

  it should "return None if decoding invalid Base32 value" in {
    Base32.decode("01") should matchPattern { case Attempt.Failure(_) => }
  }
}
