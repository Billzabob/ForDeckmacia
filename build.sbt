lazy val core = project
  .in(file("."))
  .settings(commonSettings, releaseSettings)

lazy val docs = project
  .in(file("myproject-docs"))
  .settings(
    commonSettings,
    mdocIn := file("docs/README.md"),
    mdocOut := file("README.md")
  )
  .dependsOn(core)
  .enablePlugins(MdocPlugin)

lazy val commonSettings = Seq(
  name := "fordeckmacia",
  organization := "com.github",
  scalaVersion := "2.13.2",
  crossScalaVersions := List(scalaVersion.value, "2.12.11"),
  libraryDependencies ++= Seq(
    "org.scodec" %% "scodec-core" % "1.11.7",
    "org.scodec" %% "scodec-cats" % "1.0.0",
    "org.scalacheck" %% "scalacheck" % "1.14.3" % Test,
    "org.scalatest" %% "scalatest" % "3.1.2" % Test,
    "org.scalatestplus" %% "scalacheck-1-14" % "3.1.2.0" % Test
  )
)

lazy val releaseSettings = Seq(
  organization := "com.github.billzabob",
  homepage := Some(url("https://github.com/billzabob/fordeckmacia")),
  licenses := Seq("LGPL-3.0" -> url("https://www.gnu.org/licenses/lgpl-3.0.en.html")),
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
