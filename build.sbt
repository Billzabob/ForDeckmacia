lazy val fordeckmacia = project.in(file("."))
  .settings(commonSettings, releaseSettings, publish / skip := true)
  .aggregate(core.jvm, core.js)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(commonSettings, releaseSettings)
  .settings(name := "fordeckmacia")

lazy val docs = project
  .in(file("fordeckmacia-docs"))
  .settings(
    commonSettings,
    mdocIn := file("docs/README.md"),
    mdocOut := file("README.md"),
    publish / skip := true
  )
  .dependsOn(core.jvm)
  .enablePlugins(MdocPlugin)

lazy val commonSettings = Seq(
  organization := "com.github",
  scalaVersion := "2.13.2",
  crossScalaVersions := List(scalaVersion.value, "2.12.11"),
  libraryDependencies ++= Seq(
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, n)) if n <= 11 => "org.scodec" %%% "scodec-core" % "1.11.4"
      case _                       => "org.scodec" %%% "scodec-core" % "1.11.7"
    },
    "org.scalacheck"    %%% "scalacheck"      % "1.14.3"  % Test,
    "org.scalatest"     %%% "scalatest"       % "3.1.2"   % Test,
    "org.scalatestplus" %%% "scalacheck-1-14" % "3.1.2.0" % Test
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
