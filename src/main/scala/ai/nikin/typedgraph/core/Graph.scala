package ai.nikin.typedgraph.core

class Graph(val edges: Set[AnyEdge]) {
  private[core] val flattenedEdges: Set[AnyEdge] = edges.flatMap(_.flatten)

}

object Graph {
  def apply(edges: AnyEdge*): Graph = new Graph(edges.toSet)
}
