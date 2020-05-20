package fordeckmacia

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
}
