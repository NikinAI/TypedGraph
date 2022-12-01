package ai.nikin.typedgraph.core

import scala.annotation.implicitNotFound

@implicitNotFound(
    """Connecting
    ${FROM}
to
    ${TO}
is not allowed with your current setup!

To enable this connectivity, add:

----
    implicit val ev = CanMakeEdge[${FROM}, ${EDGE}, ${TO}](factory = new EdgeFactory[${EDGE}] {})
----

In the scope of:
  """)
case class CanMakeEdge[
    FROM <: Vertex[FROM],
    EDGE[A <: Vertex[A], B <: VertexTO[A, B]] <: Edge[A, EDGE, B],
    TO <: VertexTO[FROM, TO],
](factory: EdgeFactory[EDGE])
