package ai.nikin.typedgraph.core

trait EdgeFactory[EDGE[A <: Vertex[A], B <: Vertex[B]] <: Edge[A, EDGE, B]] {
  def apply[
      FROM <: Vertex[FROM],
      TO <: Vertex[TO],
  ](
      from:      FROM,
      to:        TO,
  )(implicit ev: CanMakeEdge[FROM, EDGE, TO]): EDGE[FROM, TO]
}
