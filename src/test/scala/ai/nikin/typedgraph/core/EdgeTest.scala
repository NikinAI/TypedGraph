package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.graphexample.Content._
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
    val e = Vertex("a") >?> Vertex("b") >?> Vertex("c")
    assertEquals(e.from.label, "a")
    assertEquals(e.to.label, "c")
  }

  test("typed creation") {
    import ConnectivityPermissions.AllVertexToEdge._
    val v1 = VertexExample[ContentA, ContentB]("v1")
    val v2 = VertexExample[ContentB, ContentC]("v2")
    val p: EdgeExample[VertexExample[ContentA, ContentB], VertexExample[ContentB, ContentC]] =
      v1 >>> v2

    assertEquals(p.from, v1)
    assertEquals(p.to, v2)
  }

  test("failed creation without implicit permission") {
    checkCompileError(compileErrors("""
      val v1 = VertexExample[ContentA, ContentB]("v1")
      val v2 = VertexExample[ContentB, ContentC]("v2")
      val p: EdgeExample[VertexExample[ContentA, ContentB], VertexExample[ContentB, ContentC]] =
        v1 >>> v2
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
    val p1: SecondEdge[VertexExample[ContentA, ContentB], SecondVertex[ContentB]] = v1 >>> v2
    assertEquals(p1.from, v1)
    assertEquals(p1.to, v2)

    val v3 = VertexExample[ContentB, ContentC]("v3")
    val p2: EdgeExample[SecondVertex[ContentB], VertexExample[ContentB, ContentC]] = v2 >>> v3
    assertEquals(p2.from, v2)
    assertEquals(p2.to, v3)

    checkCompileError(compileErrors("""
      v1 >>> VertexExample[ContentB, ContentC]("abc")
    """))("Connecting", "is not allowed with your current setup!",
      "To enable this connectivity, add", "implicit", "CanMakeEdge", ">>>", "VertexExample")

    checkCompileError(compileErrors("""
      v2 >>> SecondVertex[ContentB]("abc")
    """))("Connecting", "is not allowed with your current setup!",
      "To enable this connectivity, add", "implicit", "CanMakeEdge", ">>>", "SecondVertex")
  }

  test("content flow constraints") {
    checkCompileError(
      compileErrors("""
      implicit def ev1[IN <: Content, OUT <: Content, B <: Content]: CanMakeEdge[VertexExample[IN, OUT], SecondEdge, SecondVertex[OUT]] =
        CanMakeEdge[VertexExample[IN, B], SecondEdge, SecondVertex[OUT]](SecondEdge)
    """)
    )("type arguments", "do not conform to method", "type parameter bounds", "VertexTO")

    checkCompileError(compileErrors("""
      implicit def ev1[IN <: Content, OUT <: Content]: CanMakeEdge[VertexExample[IN, OUT], SecondEdge, SecondVertex[OUT]] =
        CanMakeEdge[VertexExample[IN, OUT], SecondEdge, SecondVertex[OUT]](SecondEdge)

      val v1 = VertexExample[ContentA, ContentB]("v1")
      val v2 = SecondVertex[ContentC]("v2")
      val p1 = v1 >>> v2
    """))(
      "inferred type arguments",
      "do not conform to method",
      "type parameter bounds",
      "VertexTO",
    )
  }
}
