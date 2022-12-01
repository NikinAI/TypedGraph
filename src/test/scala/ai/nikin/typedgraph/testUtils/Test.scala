package ai.nikin.typedgraph.testUtils

abstract class Test(name: String) extends munit.FunSuite {
  def checkCompileError(compileError: String)(lookFor: String*): Unit =
    lookFor.foreach(s => assert(compileError.contains(s), s -> compileError))

  // https://scalameta.org/munit/docs/tests.html#customize-test-name-based-on-a-dynamic-condition
  override def munitTestTransforms: List[TestTransform] =
    super.munitTestTransforms ++
      List(
        new TestTransform("append Test name", { test => test.withName(s"$name - ${test.name}") })
      )
}
