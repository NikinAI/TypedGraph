package ai.nikin.typedgraph.core

class Graph(val edges: Set[Edge]) {}

object Graph {
  def apply(edges: Edge*): Graph = new Graph(edges.toSet)
}
