package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.graphexample.Content._
import ai.nikin.typedgraph.graphexample._
import ai.nikin.typedgraph.testutils.Test

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
    val p = Vertex("a") >?> Vertex("b") >?> Vertex("c") >?> Vertex("d")
    assertEquals(p.from.label, "a")
    assertEquals(p.to.label, "d")
    assertEquals(p.vertices.map(_.label), List("a", "b", "c", "d"))
  }

  test("typed creation") {
    import ConnectivityPermissions.AllVertexToEdge._

    val v1 = VertexExample[ContentA, ContentB]("v")
    val v2 = VertexExample[ContentB, ContentC]("v2")
    val v3 = VertexExample[ContentC, ContentD]("v3")
    val p: Path[VertexExample[ContentA, ContentB], VertexExample[ContentC, ContentD]] =
      v1 >>> v2 >>> v3

    assertEquals(p.from, v1)
    assertEquals(p.to, v3)
  }

  test("failed creation without implicit permission") {
    checkCompileError(compileErrors("""
      val v1 = VertexExample[ContentA, ContentB]("v")
      val v2 = VertexExample[ContentB, ContentC]("v2")
      val v3 = VertexExample[ContentC, ContentD]("v3")
      val p: Path[VertexExample[ContentA, ContentB], VertexExample[ContentC, ContentD]] =
        v1 >>> v2 >>> v3
    """))("Connecting", "is not allowed with your current setup!",
      "To enable this connectivity, add", "implicit", "CanMakeEdge", ">>>")
  }

  test("tight permission") {
    implicit def ev1[IN <: Content, OUT <: Content]: CanMakeEdge[VertexExample[IN, OUT], SecondEdge, SecondVertex[OUT]] =
      CanMakeEdge[VertexExample[IN, OUT], SecondEdge, SecondVertex[OUT]](SecondEdge)

    implicit def ev2[IN <: Content, OUT <: Content]: CanMakeEdge[SecondVertex[IN], EdgeExample, VertexExample[IN, OUT]] =
      CanMakeEdge[SecondVertex[IN], EdgeExample, VertexExample[IN, OUT]](EdgeExample)

    val v1 = VertexExample[ContentA, ContentB]("v1")
    val v2 = SecondVertex[ContentB]("v2")
    val v3 = VertexExample[ContentB, ContentC]("v3")
    val p1: Path[VertexExample[ContentA, ContentB], VertexExample[ContentB, ContentC]] =
      v1 >>> v2 >>> v3
    assertEquals(p1.from, v1)
    assertEquals(p1.to, v3)

    val v4 = SecondVertex[ContentC]("v4")
    val p2: Path[SecondVertex[ContentB], SecondVertex[ContentC]] = v2 >>> v3 >>> v4
    assertEquals(p2.from, v2)
    assertEquals(p2.to, v4)

    checkCompileError(compileErrors("""
      v2 >>> v3 >>> VertexExample[ContentC, ContentD]("abc")
    """))("Connecting", "is not allowed with your current setup!",
      "To enable this connectivity, add", "implicit", "CanMakeEdge", ">>>", "VertexExample")

    checkCompileError(compileErrors("""
      v1 >>> v2 >>> SecondVertex[ContentB]("abc")
    """))("Connecting", "is not allowed with your current setup!",
      "To enable this connectivity, add", "implicit", "CanMakeEdge", ">>>", "SecondVertex")
  }
}
