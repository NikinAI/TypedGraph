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

    assertEquals(g.findAncestors(v3), ListSet(v2))
  }

  test("foldLeft") {
    val v1 = Vertex("1")
    val v2 = Vertex("2")
    val v3 = Vertex("3")
    val e1 = v1 >?> v2
    val e2 = v2 >?> v3

    val g = e1 |+| e2

    val counter =
      g.foldVertexLeft(0) {
        case (counter, v) if v == v1 =>
          assert(counter == 0, counter)
          counter + 1
        case (counter, v) if v == v2 =>
          assert(counter == 1, counter)
          counter + 1
        case (counter, v) if v == v3 =>
          assert(counter == 2, counter)
          counter + 1
        case (counter, v)            => fail(s"Unexpected to find ($counter, $v) here.")
      }
    assert(counter == 3, counter)

    assertEquals(g.findAncestors(v2), ListSet(v1))
  }

  test("foldLeft with 2 paths") {
    val v1  = Vertex("v1")
    val v2a = Vertex("v2a")
    val v2b = Vertex("v2b")
    val v3  = Vertex("v3")

    val g: Graph = (v1 >?> v2a >?> v3) |+| (v1 >?> v2b >?> v3)

    val counter =
      g.foldVertexLeft(0) {
        case (counter, v) if v == v1  =>
          assert(counter == 0, counter)
          counter + 1
        case (counter, v) if v == v2a =>
          assert(counter == 1, counter)
          counter + 1
        case (counter, v) if v == v2b =>
          assert(counter == 2, counter)
          counter + 1
        case (counter, v) if v == v3  =>
          assert(counter == 3, counter)
          counter + 1
        case v                        => fail(s"Unexpected to find $v here.")
      }
    assert(counter == 4, counter)

    assertEquals(g.findAncestors(v3), ListSet(v2a, v2b))
  }

  test("cycle with start") {
    val a        = Vertex("a")
    val b        = Vertex("b")
    val c        = Vertex("c")
    val entrance = Vertex("entrance")

    val g: Graph = (entrance >?> a >?> b >?> c >?> a).asGraph

    val edgesCount = g.foldEdgeLeft(0)((c, _) => c + 1)
    assert(edgesCount == 4, edgesCount)

    val vertexCount = g.foldVertexLeft(0)((c, _) => c + 1)
    assert(vertexCount == 4, vertexCount)

    assertEquals(g.findAncestors(a), ListSet(entrance, c))
  }
}
