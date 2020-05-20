package fordeckmacia

// Probably a really inefficient and awful way to do this but it works for now :)
object Base32 {
  val chars = ('A' to 'Z') ++ ('2' to '7')
  val bytes = 0 to 31

  val bytesToChars = (bytes zip chars).toMap
  val charsToBytes = (chars zip bytes).toMap

  def encode(bytes: List[Byte]): String = {
    val binaryString = bytes
      .map(
        _.toInt.toBinaryString
          .takeRight(8)
          .reverse
          .padTo(8, "0")
          .reverse
          .mkString
      )
      .foldLeft("")(_ + _)

    binaryString
      .padTo(binaryString.length + 5 - (binaryString.length % 5), "0")
      .mkString
      .grouped(5)
      .map(Integer.parseInt(_, 2))
      .map(bytesToChars(_))
      .foldLeft("")(_ + _)
  }

  def decode(plainText: String): List[Byte] = {
    plainText
      .map(charsToBytes(_))
      .map(_.toBinaryString.reverse.padTo(5, "0").reverse.mkString)
      .foldLeft("")(_ + _)
      .dropRight((plainText.length * 5) - (((plainText.length * 5) / 8) * 8))
      .grouped(8)
      .toList
      .map(Integer.parseInt(_, 2))
      .map(_.toByte)
  }
}
