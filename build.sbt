lazy val root =
  project
    .in(file("."))
    .enablePlugins(GitVersioning)
    .enablePlugins(BuildInfoPlugin)
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
      coverageEnabled                        := true,

      // https://github.com/target/data-validator/blob/d3ae90ea1c84d922e50ad097f517e44852711c1c/build.sbt#LL11-L12C27
      git.useGitDescribe := true,
      // https://github.com/target/data-validator/blob/d3ae90ea1c84d922e50ad097f517e44852711c1c/build.sbt#LL27
      publishTo          := githubPublishTo.value,

      // https://github.com/djspiewak/sbt-github-packages#usage
      githubOwner       := "NikinAI",
      githubRepository  := "TypedGraph",
      publishMavenStyle := true,
      // https://github.com/djspiewak/sbt-github-packages/issues/24#issuecomment-898423448
      githubTokenSource :=
        TokenSource.Or(
          // Injected during a github workflow for publishing
          TokenSource.Environment("GITHUB_TOKEN"),

          // local token set in ~/.gitconfig
          TokenSource.GitConfig("github.token"),
        ),
    )
