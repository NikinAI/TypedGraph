lazy val root =
  project
    .in(file("."))
    .settings(
      scalaVersion                           := "2.13.10",
      name                                   := "Typed Graph",
      organization                           := "ai.nikin",
      organizationName                       := "NikinAI",
      libraryDependencies += "org.scalameta" %% "munit" % "0.7.29",
      addCommandAlias("runScalafmt", ";scalafmtAll;scalafmtSbt"),
      scalacOptions ++=
        Seq(
          "-Xlint:adapted-args",
          // Warn if an argument list is modified to match the receiver.
          "-Xlint:nullary-unit",
          // Warn when nullary methods return Unit.
          "-Xlint:inaccessible",
          // Warn about inaccessible types in method signatures.
          "-Xlint:infer-any",
          // Warn when a type argument is inferred to be Any.
          "-Xlint:constant",
          // Evaluation of a constant arithmetic expression results in an error.
          "-Ywarn-unused:imports", "-Xfatal-warnings", "-deprecation", "-Ywarn-dead-code",
          "-Ywarn-unused:params", "-Ywarn-unused:locals", "-Ywarn-value-discard",
          "-Ywarn-unused:privates",
        ),
    )
