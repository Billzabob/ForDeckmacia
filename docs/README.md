<img align="right" src="https://github.com/Billzabob/ForDeckmacia/blob/master/src/main/resources/demacia.png" height="125px" style="padding-left: 20px"/>


[![](https://github.com/Billzabob/ForDeckmacia/workflows/build/badge.svg)](https://github.com/Billzabob/ForDeckmacia/actions?query=workflow%3Abuild)
[![](https://codecov.io/gh/Billzabob/ForDeckmacia/branch/master/graph/badge.svg)](https://codecov.io/gh/Billzabob/ForDeckmacia)
[![](https://img.shields.io/nexus/r/com.github.billzabob/fordeckmacia_2.13?color=success&nexusVersion=2&server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/content/repositories/releases/com/github/billzabob/fordeckmacia_2.13)

# For Deckmacia!

A Scala implementation of [Legends of Runeterra deck codes](https://developer.riotgames.com/docs/lor#deck-codes)

```
// available for 2.12, 2.13
libraryDependencies += "com.github.billzabob" %% "fordeckmacia" % "version"
```

### Example ###

```scala mdoc
import fordeckmacia._

val deckcode = "CIAQEAIABQRQAAA"

val deck = Deck.decode(deckcode)

val cardCodes = deck.map(_.cards.map(_.code))

val encoded = deck.flatMap(_.encode)
```

<img src="https://github.com/Billzabob/ForDeckmacia/blob/master/src/main/resources/ForDeckmacia.png" height="300px"/>
