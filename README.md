<img align="right" src="https://github.com/Billzabob/ForDeckmacia/blob/master/src/main/resources/demacia.png" height="125px" style="padding-left: 20px"/>


[![](https://github.com/Billzabob/ForDeckmacia/workflows/build/badge.svg)](https://github.com/Billzabob/ForDeckmacia/actions?query=workflow%3Abuild)
[![](https://codecov.io/gh/Billzabob/ForDeckmacia/branch/master/graph/badge.svg)](https://codecov.io/gh/Billzabob/ForDeckmacia)
[![](https://img.shields.io/nexus/r/https/oss.sonatype.org/com.github.billzabob/fordeckmacia_2.13.svg)](https://oss.sonatype.org/content/repositories/releases/com/github/billzabob/fordeckmacia_2.13)

# For Deckmacia!

A Scala implementation of Legends of Runeterra deck codes

```
// available for 2.12, 2.13
libraryDependencies += "com.github.billzabob" %% "fordeckmacia" % "version"
```

### Example ###

```scala
import fordeckmacia._

val deckcode = "CEBAKAIECETSQNBWA4AQGBYMB4PCKKBXAIAQCBA4AEAQGGIAA"

val deck = Deck.decode(deckcode)
// deck: scodec.Attempt[Deck] = Successful(
//   Deck(
//     List(
//       Card(1, PiltoverAndZaun, 17),
//       Card(1, PiltoverAndZaun, 17),
//       Card(1, PiltoverAndZaun, 17),
//       Card(1, PiltoverAndZaun, 39),
//       Card(1, PiltoverAndZaun, 39),
//       Card(1, PiltoverAndZaun, 39),
//       ...
//     )
//   )  

val cardCodes = deck.map(_.cards.map(_.code))
// cardCodes: scodec.Attempt[List[String]] = Successful(
//   List(
//     "01PZ017",
//     "01PZ017",
//     "01PZ017",
//     "01PZ039",
//     "01PZ039",
//     "01PZ039",
//     ...
//   )
// )

val encoded = deck.flatMap(_.encode)
// encoded: scodec.Attempt[String] = Successful("CIBAKAIECETSQNBWA4AQGBYMB4PCKKBXAIAQCBA4AEAQGGIAA")
```

<img src="https://github.com/Billzabob/ForDeckmacia/blob/master/src/main/resources/ForDeckmacia.png" height="300px"/>
