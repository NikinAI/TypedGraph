package ai.nikin.typedgraph.core

class Path(edges: Set[Edge]) {
  lazy val from: Vertex = edges.head.from
  lazy val to:   Vertex = edges.last.to

  def >>>(next: Vertex): Path = new Path(edges + (to >>> next))
}

object Path {
  def apply(edges: Edge*): Path = new Path(edges.toSet)
}
