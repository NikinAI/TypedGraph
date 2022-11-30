package ai.nikin.typedgraph.core

class Path(val edges: List[Edge]) {
  lazy val from:     Vertex       = edges.head.from
  lazy val to:       Vertex       = edges.last.to
  lazy val vertices: List[Vertex] = from :: edges.map(_.to)

  def >>>(next: Vertex): Path = Path(edges, to >>> next)
}

object Path {
  def apply(edges: Edge*): Path = new Path(edges.toList)
  def apply(edges: List[Edge], moreEdges: Edge*): Path = new Path(edges ++ moreEdges)
}
