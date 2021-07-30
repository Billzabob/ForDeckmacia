<img align="right" src="https://github.com/Billzabob/ForDeckmacia/blob/master/core/src/main/resources/demacia.png" height="125px" style="padding-left: 20px"/>

[![](https://github.com/Billzabob/ForDeckmacia/workflows/build/badge.svg)](https://github.com/Billzabob/ForDeckmacia/actions?query=workflow%3Abuild)
[![](https://codecov.io/gh/Billzabob/ForDeckmacia/branch/master/graph/badge.svg)](https://codecov.io/gh/Billzabob/ForDeckmacia)
[![](https://img.shields.io/maven-central/v/com.github.billzabob/fordeckmacia_3.svg?color=success)](https://mvnrepository.com/artifact/com.github.billzabob/fordeckmacia)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/04e1b66676f54bb18bddad9f2de7145f)](https://www.codacy.com/manual/Billzabob/ForDeckmacia?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Billzabob/ForDeckmacia&amp;utm_campaign=Badge_Grade)

# For Deckmacia

A Scala implementation of [Legends of Runeterra deck codes](https://developer.riotgames.com/docs/lor#deck-codes)

```scala
// available for 2.12, 2.13, 3.0, and ScalaJS 1.x
libraryDependencies += "com.github.billzabob" %% "fordeckmacia" % "version"
```

## Example

```scala
import fordeckmacia.*

val deckCode = "CIBAKAYJFEYDUR2RAYAQAFI2DUSSWLICAEBAAAICAMERGJABAEBQSVI"
// deckCode: String = "CIBAKAYJFEYDUR2RAYAQAFI2DUSSWLICAEBAAAICAMERGJABAEBQSVI"

val deck = Deck.decode(deckCode)
// deck: Attempt[Deck] = Successful(
//   Deck(
//     HashMap(
//       Card(1, Demacia, 43) -> 3,
//       Card(2, Demacia, 1) -> 2,
//       Card(1, Demacia, 29) -> 3,
//       Card(3, MountTargon, 71) -> 3,
//       Card(1, Demacia, 45) -> 3,
//       Card(1, Demacia, 37) -> 3,
//       Card(1, Demacia, 21) -> 3,
//       Card(3, MountTargon, 81) -> 3,
//       Card(3, MountTargon, 36) -> 2,
//       Card(3, MountTargon, 48) -> 3,
//       Card(3, MountTargon, 41) -> 3,
//       Card(3, MountTargon, 19) -> 2,
//       Card(3, MountTargon, 85) -> 1,
//       Card(1, Demacia, 26) -> 3,
//       Card(3, MountTargon, 58) -> 3
//     )
//   )
// )

val cardCodes = deck.map(_.codes)
// cardCodes: Attempt[Map[String, Int]] = Successful(
//   HashMap(
//     "01DE026" -> 3,
//     "03MT019" -> 2,
//     "01DE045" -> 3,
//     "03MT081" -> 3,
//     "03MT036" -> 2,
//     "01DE029" -> 3,
//     "01DE021" -> 3,
//     "01DE043" -> 3,
//     "03MT058" -> 3,
//     "01DE037" -> 3,
//     "03MT041" -> 3,
//     "03MT071" -> 3,
//     "03MT048" -> 3,
//     "03MT085" -> 1,
//     "02DE001" -> 2
//   )
// )

val encoded = deck.flatMap(_.encode)
// encoded: Attempt[String] = Successful(
//   "CIBAKAYJFEYDUR2RAYAQAFI2DUSSWLICAEBAAAICAMERGJABAEBQSVI"
// )
```

<img src="https://github.com/Billzabob/ForDeckmacia/blob/master/core/src/main/resources/ForDeckmacia.png" height="300px"/>

For Deckmacia isn't endorsed by Riot Games and doesn't reflect the views or opinions of Riot Games or anyone officially involved in producing or managing Riot Games properties. Riot Games, and all associated properties are trademarks or registered trademarks of Riot Games, Inc.