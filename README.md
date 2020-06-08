<img align="right" src="https://github.com/Billzabob/ForDeckmacia/blob/master/core/src/main/resources/demacia.png" height="125px" style="padding-left: 20px"/>


[![](https://github.com/Billzabob/ForDeckmacia/workflows/build/badge.svg)](https://github.com/Billzabob/ForDeckmacia/actions?query=workflow%3Abuild)
[![](https://codecov.io/gh/Billzabob/ForDeckmacia/branch/master/graph/badge.svg)](https://codecov.io/gh/Billzabob/ForDeckmacia)
[![](https://img.shields.io/maven-central/v/com.github.billzabob/fordeckmacia_2.13.svg?color=success)](https://mvnrepository.com/artifact/com.github.billzabob/fordeckmacia)

# For Deckmacia!

A Scala implementation of [Legends of Runeterra deck codes](https://developer.riotgames.com/docs/lor#deck-codes)

```
// available for 2.12, 2.13, and ScalaJS (1.x and 0.6.x)
libraryDependencies += "com.github.billzabob" %% "fordeckmacia" % "version"
```

### Example ###

```scala
import fordeckmacia._

val deckcode = "CIBQCAQFBIBACBI5FACQEBQ5E4XTKOADAIBAMJJMAIBAKBYIAIAQKGJWAMAQEBRWAEBAKAYCAECSWNA"
// deckcode: String = "CIBQCAQFBIBACBI5FACQEBQ5E4XTKOADAIBAMJJMAIBAKBYIAIAQKGJWAMAQEBRWAEBAKAYCAECSWNA"

val deck = Deck.decode(deckcode)
// deck: scodec.Attempt[Deck] = Successful(
//   Deck(
//     HashMap(
//       Card(1, ShadowIsles, 52) -> 1,
//       Card(2, ShadowIsles, 7) -> 2,
//       Card(2, Bilgewater, 56) -> 3,
//       Card(1, ShadowIsles, 54) -> 2,
//       Card(1, ShadowIsles, 40) -> 3,
//       Card(2, Bilgewater, 37) -> 2,
//       Card(2, Bilgewater, 39) -> 3,
//       Card(2, Bilgewater, 44) -> 2,
//       Card(1, ShadowIsles, 25) -> 2,
//       Card(1, ShadowIsles, 43) -> 1,
//       Card(2, Bilgewater, 53) -> 3,
//       Card(1, ShadowIsles, 29) -> 3,
//       Card(2, ShadowIsles, 8) -> 2,
//       Card(2, ShadowIsles, 3) -> 1,
//       Card(2, ShadowIsles, 10) -> 3,
//       Card(2, Bilgewater, 29) -> 3,
//       Card(2, Bilgewater, 47) -> 3,
//       Card(2, Bilgewater, 54) -> 1
//     )
//   )
// )

val cardCodes = deck.map(_.codes)
// cardCodes: scodec.Attempt[Map[String, Int]] = Successful(
//   HashMap(
//     "02BW044" -> 2,
//     "02BW029" -> 3,
//     "01SI054" -> 2,
//     "02BW056" -> 3,
//     "01SI043" -> 1,
//     "02BW054" -> 1,
//     "01SI025" -> 2,
//     "01SI040" -> 3,
//     "02BW053" -> 3,
//     "02BW047" -> 3,
//     "01SI029" -> 3,
//     "02SI010" -> 3,
//     "02SI008" -> 2,
//     "02BW037" -> 2,
//     "01SI052" -> 1,
//     "02SI007" -> 2,
//     "02BW039" -> 3,
//     "02SI003" -> 1
//   )
// )

val encoded = deck.flatMap(_.encode)
// encoded: scodec.Attempt[String] = Successful(
//   "CIBQCAQFBIBACBI5FACQEBQ5E4XTKOADAIBAMJJMAIBAKBYIAIAQKGJWAMAQEBRWAEBAKAYCAECSWNA"
// )
```

<img src="https://github.com/Billzabob/ForDeckmacia/blob/master/core/src/main/resources/ForDeckmacia.png" height="300px"/>
