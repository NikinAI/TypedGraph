package ai.nikin.typedgraph.core

trait EdgeFactory[EDGE[A <: Vertex[A], B <: Vertex[B] { type IN = A#OUT }] <: Edge[A, EDGE, B]] {
  def apply[
      FROM <: Vertex[FROM],
      TO <: Vertex[TO] { type IN = FROM#OUT },
  ](
      from:      FROM,
      to:        TO,
  )(implicit ev: CanMakeEdge[FROM, EDGE, TO]): EDGE[FROM, TO]
}
