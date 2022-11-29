

lazy val root = project
  .in(file("."))
  .settings(
    name                  := "Typed Graph",
    scalaVersion          := "2.13.10",
    organization          := "ai.nikin",
    organizationName      := "NikinAI",
    libraryDependencies   += "org.scalameta" %% "munit" % "0.7.29",
    testFrameworks        += new TestFramework("munit.Framework"),
  )
