package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.graphExample._
import ai.nikin.typedgraph.testUtils.Test

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
    import ConnectivityPermissions.AllVertexToEdge._

    val v1 = VertexExample("v")
    val v2 = VertexExample("v2")
    val v3 = VertexExample("v3")
    val p: Path[VertexExample, VertexExample] = v1 >>> v2 >>> v3

    assertEquals(p.from, v1)
    assertEquals(p.to, v3)
  }

  test("failed creation without implicit permission") {
    checkCompileError(compileErrors("""
      val v1 = VertexExample("v")
      val v2 = VertexExample("v2")
      val v3 = VertexExample("v3")
      val p: Path[VertexExample, VertexExample] = v1 >>> v2 >>> v3
    """))("could not find implicit value for parameter", ">>>")
  }

  test("tight permission") {
    implicit val ev1: CanMakeEdge[VertexExample, SecondEdge, SecondVertex] =
      CanMakeEdge[VertexExample, SecondEdge, SecondVertex](SecondEdge)

    implicit val ev2: CanMakeEdge[SecondVertex, EdgeExample, VertexExample] =
      CanMakeEdge[SecondVertex, EdgeExample, VertexExample](EdgeExample)

    val v1 = VertexExample("v1")
    val v2 = SecondVertex("v2")
    val p1: Path[VertexExample, VertexExample] = v1 >>> v2 >>> v1
    assertEquals(p1.from, v1)
    assertEquals(p1.to, v1)

    val p2: Path[SecondVertex, SecondVertex] = v2 >>> v1 >>> v2
    assertEquals(p2.from, v2)
    assertEquals(p2.to, v2)

    checkCompileError(compileErrors("""
      v2 >>> v1 >>> VertexExample("abc")
    """))("could not find implicit value for parameter", ">>>", "VertexExample")

    checkCompileError(compileErrors("""
      v1 >>> v2 >>> SecondVertex("abc")
    """))("could not find implicit value for parameter", ">>>", "SecondVertex")
  }
}
