package ai.nikin.typedgraph.core

sealed abstract class TypelessPath(
    private[core] val edges: List[AnyEdge]
) { self =>
  private[core] def from:                AnyVertex       = edges.head.from
  private[core] def to:                  AnyVertex       = edges.last.to
  lazy final private[core] val vertices: List[AnyVertex] = from :: edges.map(_.to)
  lazy final private[core] val dropType: AnyPath         = self

  final private[core] def >?>(next: AnyVertex): AnyPath = addEdge(to >?> next)

  final private[core] def addEdge(e: AnyEdge): AnyPath = new TypelessPath(edges :+ e) {}
}

class Path[FROM <: Vertex[FROM], TO <: Vertex[TO]](
    override private[core] val edges: List[AnyEdge]
) extends TypelessPath(edges) {
  lazy final override private[core] val from: FROM = super.from.asInstanceOf[FROM]
  lazy final override private[core] val to:   TO   = super.to.asInstanceOf[TO]

  final def >>>[
      V <: VertexTO[TO, V],
      EDGE[A <: Vertex[A], B <: VertexTO[A, B]] <: Edge[A, EDGE, B],
  ](next: V)(implicit ev: CanMakeEdge[TO, EDGE, V]): Path[FROM, V] =
    addEdge[TO, EDGE, V](to >>> next)

  final private[core] def addEdge[
      F <: Vertex[F],
      EDGE[A <: Vertex[A], B <: VertexTO[A, B]] <: Edge[A, EDGE, B],
      T <: VertexTO[F, T],
  ](e: EDGE[F, T]): Path[FROM, T] = new Path(edges :+ e.dropType)
}

object Path {
  def apply[
      FROM <: Vertex[FROM],
      EDGE[A <: Vertex[A], B <: VertexTO[A, B]] <: Edge[A, EDGE, B],
      TO <: VertexTO[FROM, TO],
  ](edge: EDGE[FROM, TO]): Path[FROM, TO] = new Path(List(edge.dropType))

  def apply(edge: AnyEdge, edges: AnyEdge*): AnyPath = new TypelessPath(edge :: edges.toList) {}
}
