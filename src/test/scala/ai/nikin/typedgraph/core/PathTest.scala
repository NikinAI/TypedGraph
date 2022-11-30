package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.core.testUtils.Test

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
  }

  test("connect") {
    val e = Vertex("a") >>> Vertex("b") >>> Vertex("c") >>> Vertex("d")
    assertEquals(e.from.label, "a")
    assertEquals(e.to.label, "d")
  }
}
