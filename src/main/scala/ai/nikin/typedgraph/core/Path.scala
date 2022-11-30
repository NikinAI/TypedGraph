package ai.nikin.typedgraph.core

sealed abstract class TypelessPath(
    private[core] val edges: List[AnyEdge]
) {
  private[core] def from:                AnyVertex       = edges.head.from
  private[core] def to:                  AnyVertex       = edges.last.to
  lazy final private[core] val vertices: List[AnyVertex] = from :: edges.map(_.to)

  private[core] def >>>(next: AnyVertex): AnyPath = addEdge(to >>> next)

  private[core] def addEdge(e: AnyEdge): AnyPath = new TypelessPath(edges :+ e) {}
}

class Path[FROM <: Vertex[FROM], TO <: Vertex[TO]](
    override private[core] val edges: List[AnyEdge]
) extends TypelessPath(edges) {
  lazy override private[core] val from: FROM = super.from.asInstanceOf[FROM]
  lazy override private[core] val to:   TO   = super.to.asInstanceOf[TO]

  def >>>[V <: Vertex[V]](next: V): Path[FROM, V] = addEdge[TO, V](to >>> next)

  private[core] def addEdge[F <: Vertex[F], T <: Vertex[T]](e: Edge[F, T]): Path[FROM, T] =
    new Path(edges :+ e)
}

object Path {
  def apply[FROM <: Vertex[FROM], TO <: Vertex[TO]](edge: Edge[FROM, TO]): Path[FROM, TO] =
    new Path(List(edge))

  def apply(edge: AnyEdge, edges: AnyEdge*): AnyPath = new TypelessPath(edge :: edges.toList) {}
}
