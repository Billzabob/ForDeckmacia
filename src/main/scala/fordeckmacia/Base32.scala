package fordeckmacia

// Probably a really inefficient and awful way to do this but it works for now :)
// Scodec currently only supports Base64 and Base58 but will support Base32 in a future release
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

  def decode(plainText: String): Option[List[Byte]] = {
    plainText.toUpperCase
      .map(charsToBytes.get)
      .foldRight(Option(List.empty[Int])) {
        case (Some(byte), Some(bytes)) => Some(byte :: bytes)
        case (_, None)                 => None
        case (None, _)                 => None
      }
      .map(
        _.map(_.toBinaryString.reverse.padTo(5, "0").reverse.mkString)
          .foldLeft("")(_ + _)
          .dropRight((plainText.length * 5) - (((plainText.length * 5) / 8) * 8))
          .grouped(8)
          .toList
          .map(Integer.parseInt(_, 2))
          .map(_.toByte)
      )
  }
}
