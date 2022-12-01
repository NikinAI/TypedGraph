package ai.nikin.typedgraph.graphexample

import ai.nikin.typedgraph.core._

class EdgeExample[
    FROM <: Vertex[FROM],
    TO <: Vertex[TO] { type IN = FROM#OUT },
] private (
    override val from: FROM,
    override val to:   TO,
)(implicit ev:         CanMakeEdge[FROM, EdgeExample, TO])
    extends Edge[FROM, EdgeExample, TO](from: FROM, to: TO) {}

object EdgeExample extends EdgeFactory[EdgeExample] {
  override def apply[
      FROM <: Vertex[FROM],
      TO <: Vertex[TO] { type IN = FROM#OUT },
  ](
      from:      FROM,
      to:        TO,
  )(implicit ev: CanMakeEdge[FROM, EdgeExample, TO]): EdgeExample[FROM, TO] =
    new EdgeExample[FROM, TO](from, to)
}
