package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.core.testUtils.{Test, VertexExample}

class EdgeTest extends Test("Edge") {
  test("create/retrieve") {
    val v1 = Vertex("a")
    val v2 = Vertex("b")
    val e  = Edge(v1, v2)

    assertEquals(e.from, v1)
    assertEquals(e.to, v2)
  }

  test("connect") {
    val e = Vertex("a") >>> Vertex("b") >>> Vertex("c")
    assertEquals(e.from.label, "a")
    assertEquals(e.to.label, "c")
  }

  test("typed creation") {
    val v1 = VertexExample("v")
    val v2 = VertexExample("v2")
    val p: Edge[VertexExample, VertexExample] = v1 >>> v2

    assertEquals(p.from, v1)
    assertEquals(p.to, v2)
  }
}
