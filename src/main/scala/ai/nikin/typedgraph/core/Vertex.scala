package ai.nikin.typedgraph.core

sealed abstract class TypelessVertex(private[core] val label: String) {
  private[core] def >>>(next: AnyVertex): TypelessEdge = Edge(this, next)
}

class Vertex[SELF <: Vertex[SELF]](override val label: String) extends TypelessVertex(label) {
  self: SELF =>
  def >>>[V <: Vertex[V]](next: V): Edge[SELF, V] = Edge(self, next)
}

object Vertex {
  def apply(label: String): AnyVertex = new TypelessVertex(label) {}
}
