package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.core.testUtils.{Test, VertexExample}

class PathTest extends Test("Path") {
  test("create/retrieve") {
    val v1 = Vertex("1")
    val v2 = Vertex("2")
    val v3 = Vertex("3")
    val e1 = Edge(v1, v2)
    val e2 = Edge(v2, v3)
    val p  = Path(e1, e2)

    assertEquals(p.from, v1)
    assertEquals(p.to, v3)
    assertEquals(p.edges, List(e1, e2))
    assertEquals(p.vertices, List(v1, v2, v3))

  }

  test("connect") {
    val p = Vertex("a") >>> Vertex("b") >>> Vertex("c") >>> Vertex("d")
    assertEquals(p.from.label, "a")
    assertEquals(p.to.label, "d")
    assertEquals(p.vertices.map(_.label), List("a", "b", "c", "d"))
  }

  test("typed creation") {
    val v1 = VertexExample("v")
    val v2 = VertexExample("v2")
    val v3 = VertexExample("v3")
    val p: Path[VertexExample, VertexExample] = v1 >>> v2 >>> v3

    assertEquals(p.from, v1)
    assertEquals(p.to, v3)
  }
}
