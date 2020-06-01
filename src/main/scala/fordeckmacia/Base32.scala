package fordeckmacia

import scodec.Attempt
import scodec.bits.BitVector
import scodec.Err
import scodec.bits.ByteVector

// Scodec currently only supports Base64 and Base58 but will support Base32 in a future release
object Base32 {
  private val chars = ('A' to 'Z') ++ ('2' to '7')
  private val bytes = 0 to 31

  private val bytesToChars = (bytes zip chars).toMap
  private val charsToBytes = (chars zip bytes).toMap

  def encode(bits: BitVector): String = {
    def go(bits: BitVector, stringSoFar: String): String =
      if (bits.isEmpty) stringSoFar
      else {
        val (h, t) = bits.splitAt(5)
        val char   = bytesToChars(h.padTo(5).toInt(false)).toString
        go(t, stringSoFar + char)
      }
    go(bits, "")
  }

  def decode(plainText: String): Attempt[ByteVector] = {
    def go(plainText: String, bitsSoFar: BitVector): Attempt[BitVector] =
      plainText.headOption match {
        case Some(char) =>
          val byte = Attempt.fromOption(charsToBytes.get(char.toUpper), Err(s"Invalid Base32 character: $char"))
          byte.map(BitVector.fromInt(_, 5)).flatMap(bits => go(plainText.tail, bitsSoFar ++ bits))
        case None => Attempt.successful(bitsSoFar)
      }
    go(plainText, BitVector.empty).map(bits => bits.take(bits.size - bits.size % 8).bytes)
  }
}
