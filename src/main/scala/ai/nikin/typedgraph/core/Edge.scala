package ai.nikin.typedgraph.core

import java.rmi.UnexpectedException
import scala.annotation.unused

sealed abstract class TypelessEdge(
    private[core] val from: AnyVertex,
    private[core] val to:   AnyVertex,
) { self =>
  override def toString: String = s"$from > $to"
  lazy final private[core] val dropType: AnyEdge = self
  private[core] def asPath:              AnyPath = Path(self)

  lazy final private[core] val flatten: Set[AnyEdge] =
    self match {
      case Edge(s: VertexCombiner[_], d: VertexCombiner[_]) =>
        throw new UnexpectedException(s"An edge should not have a combiner ($s) to a combiner ($d)")
      case Edge(s: VertexCombiner[_], d)                    => s.vertices.map(Edge(_, d)).toSet
      case Edge(s, d: VertexCombiner[_])                    => d.vertices.map(Edge(s, _)).toSet
      case e                                                => Set(e)
    }

  final private[core] def >?>(next: AnyVertex): AnyPath = asPath >?> next
}

class Edge[
    FROM <: Vertex[FROM],
    EDGE[A <: Vertex[A], B <: VertexTO[A, B]] <: Edge[A, EDGE, B],
    TO <: VertexTO[FROM, TO],
](
    override private[core] val from: FROM,
    override private[core] val to:   TO,
)(implicit @unused ev:               CanMakeEdge[FROM, EDGE, TO])
    extends TypelessEdge(from, to) { self: EDGE[FROM, TO] =>
  lazy final override private[core] val asPath: Path[FROM, TO] = Path[FROM, EDGE, TO](self)

  final def >>>[
      V <: VertexTO[TO, V],
      EDGE[A <: Vertex[A], B <: VertexTO[A, B]] <: Edge[A, EDGE, B],
  ](next: V)(implicit ev: CanMakeEdge[TO, EDGE, V]): Path[FROM, V] = asPath >>> next
}

object Edge {
  def apply(from: AnyVertex, to: AnyVertex): AnyEdge = new TypelessEdge(from, to) {}

  private[core] def unapply(e: AnyEdge): Some[(AnyVertex, AnyVertex)] = Some((e.from, e.to))
}
