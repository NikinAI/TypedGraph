package ai.nikin.typedgraph.core

class Graph(val edges: Set[AnyEdge]) {}

object Graph {
  def apply(edges: AnyEdge*): Graph = new Graph(edges.toSet)
}
