package ai.nikin.typedgraph.graphexample

import ai.nikin.typedgraph.core._

class SecondEdge[
    FROM <: Vertex[FROM],
    TO <: Vertex[TO],
] private (
    override val from: FROM,
    override val to:   TO,
)(implicit ev:         CanMakeEdge[FROM, SecondEdge, TO])
    extends Edge[FROM, SecondEdge, TO](from: FROM, to: TO) {}

object SecondEdge extends EdgeFactory[SecondEdge] {
  override def apply[FROM <: Vertex[FROM], TO <: Vertex[TO]](
      from:      FROM,
      to:        TO,
  )(implicit ev: CanMakeEdge[FROM, SecondEdge, TO]): SecondEdge[FROM, TO] =
    new SecondEdge[FROM, TO](from, to)
}
