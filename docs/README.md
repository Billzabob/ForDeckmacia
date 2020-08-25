<img align="right" src="https://github.com/Billzabob/ForDeckmacia/blob/master/core/src/main/resources/demacia.png" height="125px" style="padding-left: 20px"/>

[![](https://github.com/Billzabob/ForDeckmacia/workflows/build/badge.svg)](https://github.com/Billzabob/ForDeckmacia/actions?query=workflow%3Abuild)
[![](https://codecov.io/gh/Billzabob/ForDeckmacia/branch/master/graph/badge.svg)](https://codecov.io/gh/Billzabob/ForDeckmacia)
[![](https://img.shields.io/maven-central/v/com.github.billzabob/fordeckmacia_2.13.svg?color=success)](https://mvnrepository.com/artifact/com.github.billzabob/fordeckmacia)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/04e1b66676f54bb18bddad9f2de7145f)](https://www.codacy.com/manual/Billzabob/ForDeckmacia?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Billzabob/ForDeckmacia&amp;utm_campaign=Badge_Grade)

# For Deckmacia

A Scala implementation of [Legends of Runeterra deck codes](https://developer.riotgames.com/docs/lor#deck-codes)

```scala
// available for 2.12, 2.13, and ScalaJS (1.x and 0.6.x)
libraryDependencies += "com.github.billzabob" %% "fordeckmacia" % "version"
```

## Example

```scala mdoc
import fordeckmacia._

val deckCode = "CMBQCAQFBIBACBI5FACQEBQ5E4XTKOADAIBAMJJMAIBAKBYIAIAQKGJWAMAQEBRWAEBAKAYCAECSWNA"

val deck = Deck.decode(deckCode)

val cardCodes = deck.map(_.codes)

val encoded = deck.flatMap(_.encode)
```

<img src="https://github.com/Billzabob/ForDeckmacia/blob/master/core/src/main/resources/ForDeckmacia.png" height="300px"/>
