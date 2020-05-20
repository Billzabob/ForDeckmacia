package fordeckmacia
import scala.collection.immutable.Nil

case class VarInt(bytes: List[Byte]) {
  def toInt: Int = VarInt.toInt(this)
}

object VarInt {
  def toInt(varInt: VarInt): Int =
    varInt.bytes.foldLeft(0)((int, byte) => (int << 7) + (byte & 0x7f))

  def fromInt(int: Int): VarInt = {
    def go(shiftAmount: Int): List[Byte] = {
      val rest = (int >> shiftAmount)
      if (rest == 0) Nil
      else {
        ((rest & 0x7f) | 0x80).toByte :: go(shiftAmount + 7)
      }
    }

    val bytes = (int & 0x7f).toByte :: go(7)

    VarInt(bytes.reverse)
  }

  def fromBytes(bytes: List[Byte]): List[VarInt] = {
    def group(bytes: List[List[Byte]], remaining: List[Byte], lastWasEnd: Boolean): List[List[Byte]] =
      remaining match {
        case head :: next if lastWasEnd =>
          group(List(head) :: bytes, next, (head & 0x80) == 0)
        case head :: next =>
          group((head :: bytes.headOption.getOrElse(Nil)) :: bytes.drop(1), next, (head & 0x80) == 0)
        case Nil =>
          bytes
      }

    group(Nil, bytes, true).reverse.map(_.reverse).map(VarInt.apply)
  }
}
