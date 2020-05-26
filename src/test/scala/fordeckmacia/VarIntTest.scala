package fordeckmacia

import org.scalacheck.Gen.posNum
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class VarIntTest extends AnyFlatSpec with Matchers with ScalaCheckDrivenPropertyChecks {
  "VarInt to int" should "be idempotent" in {
    forAll(posNum[Int]) { int: Int =>
      val encoded   = VarInt.fromInt(int)
      val decoded   = VarInt.toInt(encoded)
      val reEncoded = VarInt.fromInt(decoded)
      decoded shouldBe int
      reEncoded shouldBe encoded
    }
  }

  "VarInt to bytes" should "be idempotent" in {
    forAll { bytes: List[Byte] =>
      val encoded   = VarInt.fromBytes(bytes)
      val decoded   = VarInt.toBytes(encoded)
      val reEncoded = VarInt.fromBytes(decoded)
      decoded shouldBe bytes
      reEncoded shouldBe encoded
    }
  }
}
