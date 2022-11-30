package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.core.testUtils.Test

class VertexTest extends Test("Vertex") {
  test("create/retrieve") {
    val label = "name"
    val v     = Vertex(label)

    assertEquals(v.label, label)
  }

  test("connect") {
    val e = Vertex("a") >>> Vertex("b")
    assertEquals(e.from.label, "a")
    assertEquals(e.to.label, "b")
  }
}
