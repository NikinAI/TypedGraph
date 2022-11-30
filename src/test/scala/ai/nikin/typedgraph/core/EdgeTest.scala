package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.core.testUtils.Test

class EdgeTest extends Test("Edge") {
  test("create/retrieve") {
    val v1 = Vertex("a")
    val v2 = Vertex("b")
    val e = Edge(v1,v2)

    assertEquals(e.from, v1)
    assertEquals(e.to, v2)
  }
}
