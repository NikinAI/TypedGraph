

lazy val root = project
  .in(file("."))
  .settings(
    scalaVersion          := "2.13.10",

    name                  := "Typed Graph",
    organization          := "ai.nikin",
    organizationName      := "NikinAI",
    
    libraryDependencies   += "org.scalameta" %% "munit" % "0.7.29",
    testFrameworks        += new TestFramework("munit.Framework"),

    addCommandAlias("runScalafmt", ";scalafmtAll;scalafmtSbt")
  )
