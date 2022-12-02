package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.testutils.Test

import scala.collection.immutable.ListSet

class GraphTest extends Test("Graph") {
  test("create/retrieve") {
    val v1 = Vertex("1")
    val v2 = Vertex("2")
    val v3 = Vertex("3")
    val e1 = Edge(v1, v2)
    val e2 = Edge(v2, v3)

    val g = Graph(e1, e2)

    assertEquals(g.edges, ListSet(e1, e2))
  }
}
