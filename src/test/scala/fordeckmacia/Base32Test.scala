package fordeckmacia

import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

object Base32Test extends Properties("Base32") {
  property("idempotent") = forAll { bytes: List[Byte] =>
    Base32.decode(Base32.encode(bytes)) == Some(bytes)
  }
}
