package fordeckmacia

import org.scalacheck.Gen.posNum
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

object VarIntTest extends Properties("VarInt") {
  property("idempotent to int") = forAll(posNum[Int]) { int: Int =>
    val encoded = VarInt.fromInt(int)
    val decoded = VarInt.toInt(encoded)
    val reEncoded = VarInt.fromInt(decoded)
    decoded == int && reEncoded == encoded
  }

  property("idempotent to bytes") = forAll { bytes: List[Byte] =>
    val encoded = VarInt.fromBytes(bytes)
    val decoded = VarInt.toBytes(encoded)
    val reEncoded = VarInt.fromBytes(decoded)
    decoded == bytes && reEncoded == encoded
  }
}
