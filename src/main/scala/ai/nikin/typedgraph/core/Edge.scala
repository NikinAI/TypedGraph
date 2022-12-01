package ai.nikin.typedgraph.core

sealed abstract class TypelessEdge(
    private[core] val from: AnyVertex,
    private[core] val to:   AnyVertex,
) { self =>
  lazy final private[core] val dropType: AnyEdge = self
  private[core] def asPath:              AnyPath = Path(self)

  private[core] def >>>(next: AnyVertex): AnyPath = asPath >>> next
}

class Edge[
    FROM <: Vertex[FROM],
    EDGE[A <: Vertex[A], B <: Vertex[B]] <: Edge[A, EDGE, B],
    TO <: Vertex[TO],
](
    override private[core] val from: FROM,
    override private[core] val to:   TO,
) extends TypelessEdge(from, to) { self: EDGE[FROM, TO] =>
  lazy override private[core] val asPath: Path[FROM, TO] = Path(self)

  def >>>[
      V <: Vertex[V],
      EDGE[A <: Vertex[A], B <: Vertex[B]] <: Edge[A, EDGE, B],
  ](next: V)(implicit factory: EdgeFactory[EDGE]): Path[FROM, V] = asPath >>> next
}

object Edge {
  def apply(from: AnyVertex, to: AnyVertex): AnyEdge = new TypelessEdge(from, to) {}
}
