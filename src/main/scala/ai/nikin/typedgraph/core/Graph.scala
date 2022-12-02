package ai.nikin.typedgraph.core

class Graph private (val edges: Set[AnyEdge]) {}

object Graph {
  def apply(edges: AnyEdge*): Graph = new Graph(edges.toSet.flatMap[AnyEdge](_.flatten))
}
