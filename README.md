<img align="right" src="https://github.com/Billzabob/ForDeckmacia/blob/master/core/src/main/resources/demacia.png" height="125px" style="padding-left: 20px"/>

[![](https://github.com/Billzabob/ForDeckmacia/workflows/build/badge.svg)](https://github.com/Billzabob/ForDeckmacia/actions?query=workflow%3Abuild)
[![](https://codecov.io/gh/Billzabob/ForDeckmacia/branch/master/graph/badge.svg)](https://codecov.io/gh/Billzabob/ForDeckmacia)
[![](https://img.shields.io/maven-central/v/com.github.billzabob/fordeckmacia_2.13.svg?color=success)](https://mvnrepository.com/artifact/com.github.billzabob/fordeckmacia)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/04e1b66676f54bb18bddad9f2de7145f)](https://www.codacy.com/manual/Billzabob/ForDeckmacia?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Billzabob/ForDeckmacia&amp;utm_campaign=Badge_Grade)

# For Deckmacia

A Scala implementation of [Legends of Runeterra deck codes](https://developer.riotgames.com/docs/lor#deck-codes)

```scala
// available for 2.12, 2.13, and ScalaJS 1.x
libraryDependencies += "com.github.billzabob" %% "fordeckmacia" % "version"
```

## Example

```scala
import fordeckmacia._

val deckCode = "CIBAKAYJFEYDUR2RAYAQAFI2DUSSWLICAEBAAAICAMERGJABAEBQSVI"
// deckCode: String = "CIBAKAYJFEYDUR2RAYAQAFI2DUSSWLICAEBAAAICAMERGJABAEBQSVI"

val deck = Deck.decode(deckCode)
// deck: scodec.Attempt[Deck] = Successful(
//   value = Deck(
//     cards = HashMap(
//       Card(set = 1, faction = Demacia, cardNumber = 43) -> 3,
//       Card(set = 2, faction = Demacia, cardNumber = 1) -> 2,
//       Card(set = 1, faction = Demacia, cardNumber = 29) -> 3,
//       Card(set = 3, faction = MountTargon, cardNumber = 71) -> 3,
//       Card(set = 1, faction = Demacia, cardNumber = 45) -> 3,
//       Card(set = 1, faction = Demacia, cardNumber = 37) -> 3,
//       Card(set = 1, faction = Demacia, cardNumber = 21) -> 3,
//       Card(set = 3, faction = MountTargon, cardNumber = 81) -> 3,
//       Card(set = 3, faction = MountTargon, cardNumber = 36) -> 2,
//       Card(set = 3, faction = MountTargon, cardNumber = 48) -> 3,
//       Card(set = 3, faction = MountTargon, cardNumber = 41) -> 3,
//       Card(set = 3, faction = MountTargon, cardNumber = 19) -> 2,
//       Card(set = 3, faction = MountTargon, cardNumber = 85) -> 1,
//       Card(set = 1, faction = Demacia, cardNumber = 26) -> 3,
//       Card(set = 3, faction = MountTargon, cardNumber = 58) -> 3
//     )
//   )
// )

val cardCodes = deck.map(_.codes)
// cardCodes: scodec.Attempt[Map[String, Int]] = Successful(
//   value = HashMap(
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
// encoded: scodec.Attempt[String] = Successful(
//   value = "CIBAKAYJFEYDUR2RAYAQAFI2DUSSWLICAEBAAAICAMERGJABAEBQSVI"
// )
```

<img src="https://github.com/Billzabob/ForDeckmacia/blob/master/core/src/main/resources/ForDeckmacia.png" height="300px"/>

For Deckmacia isn't endorsed by Riot Games and doesn't reflect the views or opinions of Riot Games or anyone officially involved in producing or managing Riot Games properties. Riot Games, and all associated properties are trademarks or registered trademarks of Riot Games, Inc.