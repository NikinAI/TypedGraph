package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.graphexample._
import ai.nikin.typedgraph.testutils.Test

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
    import ConnectivityPermissions.AllVertexToEdge._
    val v1 = VertexExample("v1")
    val v2 = VertexExample("v2")
    val p: EdgeExample[VertexExample, VertexExample] = v1 >>> v2

    assertEquals(p.from, v1)
    assertEquals(p.to, v2)
  }

  test("failed creation without implicit permission") {
    checkCompileError(compileErrors("""
      val v1 = VertexExample("v1")
      val v2 = VertexExample("v2")
      val p: EdgeExample[VertexExample, VertexExample] = v1 >>> v2
    """))("could not find implicit value for parameter", ">>>")
  }

  test("tight permission") {
    implicit val ev1: CanMakeEdge[VertexExample, SecondEdge, SecondVertex] =
      CanMakeEdge[VertexExample, SecondEdge, SecondVertex](SecondEdge)

    implicit val ev2: CanMakeEdge[SecondVertex, EdgeExample, VertexExample] =
      CanMakeEdge[SecondVertex, EdgeExample, VertexExample](EdgeExample)

    val v1 = VertexExample("v1")
    val v2 = SecondVertex("v2")
    val p1: SecondEdge[VertexExample, SecondVertex] = v1 >>> v2
    assertEquals(p1.from, v1)
    assertEquals(p1.to, v2)

    val p2: EdgeExample[SecondVertex, VertexExample] = v2 >>> v1
    assertEquals(p2.from, v2)
    assertEquals(p2.to, v1)

    checkCompileError(compileErrors("""
      v1 >>> VertexExample("abc")
    """))("could not find implicit value for parameter", ">>>", "VertexExample")

    checkCompileError(compileErrors("""
      v2 >>> SecondVertex("abc")
    """))("could not find implicit value for parameter", ">>>", "SecondVertex")
  }
}
