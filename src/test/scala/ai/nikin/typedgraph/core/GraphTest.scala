package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.testutils.Test

class GraphTest extends Test("Graph") {
  test("create/retrieve") {
    val v1 = Vertex("1")
    val v2 = Vertex("2")
    val v3 = Vertex("3")
    val e1 = Edge(v1, v2)
    val e2 = Edge(v2, v3)

    val g = Graph(e1, e2)

    assertEquals(g.edges, Set(e1, e2))
  }
}
