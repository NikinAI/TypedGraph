package ai.nikin.typedgraph.core.testUtils

import ai.nikin.typedgraph.core.{Edge, EdgeFactory, Vertex}

case class EdgeExample[
    FROM <: Vertex[FROM],
    TO <: Vertex[TO],
](
    override val from: FROM,
    override val to:   TO,
) extends Edge[FROM, EdgeExample, TO](from: FROM, to: TO) {}

object EdgeExample {
  implicit val factory: EdgeFactory[EdgeExample] =
    new EdgeFactory[EdgeExample] {
      override def apply[FROM <: Vertex[FROM], TO <: Vertex[TO]](
          from: FROM,
          to:   TO,
      ): EdgeExample[FROM, TO] = new EdgeExample[FROM, TO](from, to)
    }
}
