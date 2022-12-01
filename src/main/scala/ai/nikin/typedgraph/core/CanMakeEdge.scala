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
    EDGE[A <: Vertex[A], B <: Vertex[B] { type IN = A#OUT }] <: Edge[A, EDGE, B],
    TO <: Vertex[TO] { type IN = FROM#OUT },
](factory: EdgeFactory[EDGE])
