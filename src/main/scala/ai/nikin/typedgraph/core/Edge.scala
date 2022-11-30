package ai.nikin.typedgraph.core

class Edge(val from: Vertex, val to: Vertex) {
  def >>>(next: Vertex): Path = Path(this, to >>> next)
}

object Edge {
  def apply(from: Vertex, to: Vertex): Edge = new Edge(from, to)
}
