package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.core.Utils._

import scala.collection.immutable.ListSet

class Graph private (val edges: ListSet[AnyEdge]) {

  lazy private[core] val leftBoundary: Set[AnyVertex] =
    flattenedEdges.map(_.from) -- flattenedEdges.map(_.to)
  lazy private[core] val rightBoundary: Set[AnyVertex] =
    flattenedEdges.map(_.to) -- flattenedEdges.map(_.from)

  lazy private[core] val leftEdgeBoundary: Set[AnyEdge] =
    leftBoundary.flatMap(findEdgesWithFrom(flattenedEdges))
  lazy private[core] val rightEdgeBoundary: Set[AnyEdge] =
    rightBoundary.flatMap(findEdgesWithTo(flattenedEdges))

  lazy private[core] val vertexPerDepth: Map[Int, Set[AnyVertex]] =
    edgePerDepth
      .foldLeft(Map.empty[Int, Set[AnyVertex]]) {
        case (acc, (k, edges)) => acc.addAt(k)(edges.map(_.from)).addAt(k + 1)(edges.map(_.to))
      }
      .cleanPreviousKeys()

  lazy private[core] val edgePerDepth: Map[Int, Set[AnyEdge]] = {
    def loop(
      depth: Int,
      acc: Map[Int, Set[AnyEdge]],
      edgesLeft: Set[AnyEdge],
      node: AnyVertex,
    ): Map[Int, Set[AnyEdge]] =
      findEdgesWithFrom(edgesLeft)(node).foldLeft(acc) {
        case (acc, e@Edge(_, to)) => loop(
          depth = depth + 1,
          acc = acc.addOneAt(depth)(e),
          edgesLeft = edgesLeft - e,
          node = to,
        )
      }

    leftEdgeBoundary
      .foldLeft(Map.empty[Int, Set[AnyEdge]]) {
        case (acc, e) => loop(
          depth = 1,
          acc = acc.addOneAt(0)(e),
          edgesLeft = self.flattenedEdges,
          node = e.to,
        )
      }
      .cleanPreviousKeys()
  }

  def foldVertexLeft[A](init: A)(f: (A, AnyVertex) => A): A = vertexPerDepth.deepFoldLeft(init)(f)

  def foldEdgeLeft[A](init: A)(f: (A, AnyEdge) => A): A = edgePerDepth.deepFoldLeft(init)(f)
}

object Graph {
  def apply(edges: AnyEdge*): Graph = new Graph(edges.toListSet.flatMap[AnyEdge](_.flatten))

  private[core] def findEdgesWithFrom(
      edges: Set[AnyEdge]
  )(from:    AnyVertex): Set[AnyEdge] = edges.flatMap(_ filterWithFrom from)

  private[core] def findEdgesWithTo(
      edges: Set[AnyEdge]
  )(to:      AnyVertex): Set[AnyEdge] = edges.flatMap(_ filterWithTo to)
}
