package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.core.Utils._

import scala.annotation.unused
import scala.collection.immutable.ListSet

class Graph private (val edges: ListSet[AnyEdge]) { self =>
  import Graph._

  // TODO: Make sure to never commit other code than `()`
  protected def debug(message: String): Unit = ()

  implicit private class MapExtra[T](m: Map[Int, ListSet[T]]) {
    def addAt(k: Int)(extra: ListSet[T]): Map[Int, ListSet[T]] =
      if(extra.isEmpty) m
      else m.updatedWith(k) {
        case None    => Some(extra)
        case Some(s) => Some(s ++ extra)
      }

    def addOneAt(k: Int)(extra: T): Map[Int, ListSet[T]] = m.addAt(k)(ListSet(extra))

    def removeOneAt(k: Int)(remove: T): Map[Int, ListSet[T]] =
      m.updatedWith(k) {
        case None    => None
        case Some(s) => s - remove match {
            case r if r.isEmpty => None
            case r              => Some(r)
          }
      }

    def cleanPreviousKeys(): Map[Int, ListSet[T]] =
      m.keys
        .filterNot(_ == 0)
        .toList
        .sortBy(-_)
        .foldLeft(m) {
          case (acc, k) => m(k).foldLeft(acc) {
              case (acc, v) => (1 to k).foldLeft(acc) { case (acc, d) => acc.removeOneAt(k - d)(v) }
            }
        }

    def deepFoldLeft[B](init: B)(f: (B, T) => B): B =
      m.toList
        .sortBy(_._1)
        .foldLeft(init) {
          case (acc, (k, v)) => v
              .toList
              .sortBy(_.toString)
              .foldLeft(acc) {
                case (acc, v) =>
                  debug(s"$k -> $v")
                  f(acc, v)
              }
        }
  }

  lazy private val leftBoundary:              ListSet[AnyVertex] = edges.map(_.from) -- edges.map(_.to)
  lazy private val rightBoundary:             ListSet[AnyVertex] = edges.map(_.to) -- edges.map(_.from)
  lazy private val leftEdgeBoundary:          ListSet[AnyEdge]   = leftBoundary.flatMap(findEdgesFrom(edges))
  @unused lazy private val rightEdgeBoundary: ListSet[AnyEdge]   =
    rightBoundary.flatMap(findEdgesTo(edges))

  lazy private val vertexPerDepth: Map[Int, ListSet[AnyVertex]] =
    edgePerDepth
      .foldLeft(Map.empty[Int, ListSet[AnyVertex]]) {
        case (acc, (k, edges)) => acc.addAt(k)(edges.map(_.from)).addAt(k + 1)(edges.map(_.to))
      }
      .cleanPreviousKeys()

  lazy private val edgePerDepth: Map[Int, ListSet[AnyEdge]] = {
    def loop(
        depth:     Int,
        acc:       Map[Int, ListSet[AnyEdge]],
        edgesLeft: ListSet[AnyEdge],
        node:      AnyVertex,
    ): Map[Int, ListSet[AnyEdge]] =
      findEdgesFrom(edgesLeft)(node).foldLeft(acc) {
        case (acc, e @ Edge(_, to)) => loop(
            depth = depth + 1,
            acc = acc.addOneAt(depth)(e),
            edgesLeft = edgesLeft - e,
            node = to,
          )
      }

    leftEdgeBoundary
      .foldLeft(Map.empty[Int, ListSet[AnyEdge]]) {
        case (acc, e) => loop(
            depth = 1,
            acc = acc.addOneAt(0)(e),
            edgesLeft = self.edges,
            node = e.to,
          )
      }
      .cleanPreviousKeys()
  }

  def foldVertexLeft[A](init: A)(f: (A, AnyVertex) => A): A = vertexPerDepth.deepFoldLeft(init)(f)
  def foldEdgeLeft[A](init:   A)(f: (A, AnyEdge) => A):   A = edgePerDepth.deepFoldLeft(init)(f)
}

object Graph {
  def apply(edges: AnyEdge*): Graph = new Graph(edges.toListSet.flatMap[AnyEdge](_.flatten))

  private def findEdgesFrom(edges: ListSet[AnyEdge])(from: AnyVertex): ListSet[AnyEdge] =
    edges.flatMap(_ filterFrom from)

  private def findEdgesTo(edges: ListSet[AnyEdge])(to: AnyVertex): ListSet[AnyEdge] =
    edges.flatMap(_ filterTo to)
}
