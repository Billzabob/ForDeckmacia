addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat"   % "0.1.11")     // Add extra compiler flags to prevent common mistakes
addSbtPlugin("org.scalameta"             % "sbt-scalafmt"   % "2.4.0")      // Format code with `sbt scalafmt`
addSbtPlugin("org.scoverage"             % "sbt-scoverage"  % "1.6.1")      // Runs code coverage for the project `sbt coverage` https://github.com/scoverage/sbt-scoverage
addSbtPlugin("com.geirsson"              % "sbt-ci-release" % "1.5.3")      // Automatically publish to Sonatype and Maven Central
