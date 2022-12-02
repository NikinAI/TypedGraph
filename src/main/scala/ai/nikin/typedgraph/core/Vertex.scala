package ai.nikin.typedgraph.core

sealed abstract class TypelessVertex(private[core] val label: String) { self =>
  lazy final private[core] val dropType: AnyVertex      = self
  private[core] def flatten:             Set[AnyVertex] = Set(self)
  override def toString:                 String         = label

  final private[core] def >?>(next: AnyVertex): TypelessEdge = Edge(this, next)
}

abstract class Vertex[SELF <: Vertex[SELF]](override val label: String)
    extends TypelessVertex(label) {
  self: SELF =>
  type IN
  type OUT

  def >>>[
      V <: VertexTO[SELF, V],
      EDGE[A <: Vertex[A], B <: VertexTO[A, B]] <: Edge[A, EDGE, B],
  ](next: V)(implicit ev: CanMakeEdge[SELF, EDGE, V]): EDGE[SELF, V] = ev.factory(self, next)

  final def &&[
      B <: Vertex[B],
      OUTPUT <: VertexCombiner[OUTPUT],
  ](b: B)(implicit ev: CanBeCombined[SELF, B, OUTPUT]): OUTPUT = ev(self, b)
}

object Vertex {
  def apply(label: String): AnyVertex = new TypelessVertex(label) {}
}

// TODO: Lots of room for improvements
abstract class VertexCombiner[
    SELF <: VertexCombiner[SELF]
](
    private[core] val vertices: List[AnyVertex]
) extends Vertex[SELF](vertices.mkString(" && ")) { self: SELF =>
  lazy final override private[core] val flatten: Set[AnyVertex] =
    vertices.toSet.flatMap[AnyVertex](_.flatten)
}
