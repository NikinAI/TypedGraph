package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.graphexample.Content._
import ai.nikin.typedgraph.graphexample.{SecondVertex, VertexExample}
import ai.nikin.typedgraph.testutils.Test

import scala.collection.immutable.ListSet

class VertexTest extends Test("Vertex") {
  test("create/retrieve") {
    val label = "name"
    val v     = Vertex(label)

    assertEquals(v.label, label)
  }

  test("connect") {
    val e = Vertex("a") >?> Vertex("b")
    assertEquals(e.from.label, "a")
    assertEquals(e.to.label, "b")
  }

  test("typed creation") {
    val v = VertexExample("v")
    assertEquals(v.n, "v")
    // Dropping the '$'
    assertEquals(v.getClass.getCanonicalName, VertexExample.getClass.getCanonicalName.dropRight(1))
  }

  test("combined 2") {
    val v1 = SecondVertex[ContentA]("v1")
    val v2 = SecondVertex[ContentB]("v2")

    class Combiner[DATA](vertices: AnyVertex*)
        extends VertexCombiner[Combiner[DATA]](vertices.toList) {
      override type IN  = DATA
      override type OUT = DATA
    }

    implicit def ev[A <: Content, B <: Content]: CanBeCombined[SecondVertex[A], SecondVertex[B], Combiner[(A, B)]] =
      new CanBeCombined[
        SecondVertex[A],
        SecondVertex[B],
        Combiner[(A, B)],
      ] {
        def apply(
            a: SecondVertex[A],
            b: SecondVertex[B],
        ): Combiner[(A, B)] = new Combiner(a, b)
      }

    val combined: Combiner[(ContentA, ContentB)] = v1 && v2
    assertEquals(combined.vertices, List(v1, v2))
  }

  test("combined 3") {
    val v1 = SecondVertex[ContentA]("v1")
    val v2 = SecondVertex[ContentB]("v2")
    val v3 = SecondVertex[ContentC]("v3")

    class Combiner[DATA](vertices: AnyVertex*)
        extends VertexCombiner[Combiner[DATA]](vertices.toList) {
      override type IN  = DATA
      override type OUT = DATA
    }

    implicit def ev[A <: Content, B <: Content]: CanBeCombined[SecondVertex[A], SecondVertex[B], Combiner[(A, B)]] =
      new CanBeCombined[
        SecondVertex[A],
        SecondVertex[B],
        Combiner[(A, B)],
      ] {
        def apply(
            a: SecondVertex[A],
            b: SecondVertex[B],
        ): Combiner[(A, B)] = new Combiner(a, b)
      }

    implicit def ev2[A, B <: Content]: CanBeCombined[Combiner[A], SecondVertex[B], Combiner[(A, B)]] =
      new CanBeCombined[
        Combiner[A],
        SecondVertex[B],
        Combiner[(A, B)],
      ] {
        def apply(
            a: Combiner[A],
            b: SecondVertex[B],
        ): Combiner[(A, B)] = new Combiner(a, b)
      }

    // TODO: Could be using shapeless or scala3 tuple to not have nested tuples.
    val combined: Combiner[((ContentA, ContentB), ContentC)] = v1 && v2 && v3
    assertEquals(combined.flatten, ListSet[AnyVertex](v1 , v2, v3))
  }
}
