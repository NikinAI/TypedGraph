package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.core.testUtils.Test

class VertexTest extends Test("Vertex") {
  test("create/retrieve") {
    val label = "name"
    val v     = Vertex(label)

    assertEquals(v.label, label)
  }
}
