package ai.nikin.typedgraph.core

import ai.nikin.typedgraph.core.Utils._

import scala.annotation.unused
import scala.collection.immutable.ListSet

sealed abstract class TypelessEdge(
    private[core] val from: AnyVertex,
    private[core] val to:   AnyVertex,
) { self =>
  override def toString:                 String  = s"$from > $to"
  lazy final private[core] val dropType: AnyEdge = self
  private[core] def asPath:              AnyPath = Path(self)

  lazy final private[core] val flatten: ListSet[AnyEdge] =
    self match {
      case Edge(s: VertexCombiner[_], d: VertexCombiner[_]) =>
        // TODO: Detect at creation time/compile time.
        throw new RuntimeException(s"An edge should not have a combiner ($s) to a combiner ($d)")

      case Edge(s: VertexCombiner[_], d) =>
        // TODO: We should be creating the right type of Edge !
        s.vertices.map(Edge(_, d)).toListSet
      case Edge(s, d: VertexCombiner[_]) =>
        // TODO: We should be creating the right type of Edge !
        d.vertices.map(Edge(s, _)).toListSet
      case e                             => ListSet(e)
    }

  final private[core] def >?>(next: AnyVertex): AnyPath = asPath >?> next
  final def |+|(path:               AnyPath):   Graph   = Graph((self +: path.edges): _*)
  final def |+|(e:                  AnyEdge):   Graph   = Graph(self, e)

  private[core] def filterFrom(v: AnyVertex): Option[AnyEdge] = if(from == v) Some(self) else None
  private[core] def filterTo(v:   AnyVertex): Option[AnyEdge] = if(to == v) Some(self) else None
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

  def unapply(e: AnyEdge): Some[(AnyVertex, AnyVertex)] = Some((e.from, e.to))

  object Triplet {
    def unapply(e: AnyEdge): Some[(AnyVertex, AnyEdge, AnyVertex)] = Some((e.from, e, e.to))
  }
}
