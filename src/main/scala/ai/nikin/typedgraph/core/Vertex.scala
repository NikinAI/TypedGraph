package ai.nikin.typedgraph.core

sealed abstract class TypelessVertex(private[core] val label: String) { self =>
  lazy final private[core] val dropType: AnyVertex = self

  private[core] def >?>(next: AnyVertex): TypelessEdge = Edge(this, next)
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
}

object Vertex {
  def apply(label: String): AnyVertex = new TypelessVertex(label) {}
}
