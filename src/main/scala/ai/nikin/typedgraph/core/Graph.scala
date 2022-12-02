package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.core.Utils._

import scala.collection.immutable.ListSet

class Graph private (val edges: ListSet[AnyEdge]) {}

object Graph {
  def apply(edges: AnyEdge*): Graph = new Graph(edges.toListSet.flatMap[AnyEdge](_.flatten))
}
