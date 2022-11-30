package ai.nikin.typedgraph.core

sealed abstract class TypelessEdge(
    private[core] val from: AnyVertex,
    private[core] val to:   AnyVertex,
) { self =>
  lazy private[core] val asPath: AnyPath = Path(self)

  private[core] def >>>(next: AnyVertex): AnyPath = asPath >>> next
}

class Edge[FROM <: Vertex[FROM], TO <: Vertex[TO]](
    override private[core] val from: FROM,
    override private[core] val to:   TO,
) extends TypelessEdge(from, to) { self =>
  lazy override private[core] val asPath: Path[FROM, TO] = Path(self)

  def >>>[V <: Vertex[V]](next: V): Path[FROM, V] = asPath >>> next
}

object Edge {
  def apply[FROM <: Vertex[FROM], TO <: Vertex[TO]](from: FROM, to: TO): Edge[FROM, TO] =
    new Edge(from, to)

  def apply(from: AnyVertex, to: AnyVertex): AnyEdge = new TypelessEdge(from, to) {}
}
