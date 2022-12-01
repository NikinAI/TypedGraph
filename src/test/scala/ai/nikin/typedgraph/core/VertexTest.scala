package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.graphExample.VertexExample
import ai.nikin.typedgraph.testUtils.Test

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

  test("typed creation") {
    val v = VertexExample("v")
    assertEquals(v.n, "v")
    // Dropping the '$'
    assertEquals(v.getClass.getCanonicalName, VertexExample.getClass.getCanonicalName.dropRight(1))
  }
}
