package fordeckmacia

import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

object Base32Test extends Properties("Base32") {
  property("idempotent") = forAll { bytes: List[Byte] =>
    val encoded = Base32.encode(bytes)
    val decoded = Base32.decode(encoded)
    val reEncoded = decoded.map(Base32.encode)
    decoded == Some(bytes) && reEncoded == Some(encoded)
  }
}
