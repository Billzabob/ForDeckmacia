addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat"             % "0.1.13")
addSbtPlugin("org.scalameta"             % "sbt-scalafmt"             % "2.4.2")
addSbtPlugin("org.scoverage"             % "sbt-scoverage"            % "1.6.1")
addSbtPlugin("com.geirsson"              % "sbt-ci-release"           % "1.5.3")
addSbtPlugin("org.scalameta"             % "sbt-mdoc"                 % "2.2.4")
addSbtPlugin("org.scala-js"              % "sbt-scalajs"              % Option(System.getenv("SCALAJS_VERSION")).getOrElse("1.1.1"))
addSbtPlugin("org.portable-scala"        % "sbt-scalajs-crossproject" % "1.0.0")
addSbtPlugin("com.typesafe"              % "sbt-mima-plugin"          % "0.7.0")

