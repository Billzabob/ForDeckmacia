lazy val core = project
  .in(file("."))
  .settings(commonSettings, releaseSettings)

lazy val commonSettings = Seq(
  name := "fordeckmacia",
  organization := "com.github",
  scalaVersion := "2.13.2",
  crossScalaVersions := List(scalaVersion.value, "2.12.11")
)

lazy val releaseSettings = Seq(
  organization := "com.github.billzabob",
  homepage := Some(url("https://github.com/billzabob/fordeckmacia")),
  developers := List(
    Developer(
      "billzabob",
      "Nick Hallstrom",
      "cocolymoo@gmail.com",
      url("https://github.com/billzabob")
    )
  )
)

Global / onChangedBuildSource := ReloadOnSourceChanges