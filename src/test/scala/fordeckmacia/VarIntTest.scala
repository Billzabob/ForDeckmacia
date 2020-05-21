package fordeckmacia

import org.scalacheck.Gen.posNum
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

object VarIntTest extends Properties("VarInt") {
  property("idempotent to int") = forAll(posNum[Int]) { int: Int =>
    VarInt.toInt(VarInt.fromInt(int)) == int
  }

  property("idempotent to bytes") = forAll { bytes: List[Byte] =>
    VarInt.toBytes(VarInt.fromBytes(bytes)) == bytes
  }
}
