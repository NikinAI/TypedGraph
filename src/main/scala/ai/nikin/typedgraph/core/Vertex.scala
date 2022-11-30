package ai.nikin.typedgraph.core

class Vertex(val label: String) {
  def >>>(next: Vertex): Edge =  Edge(this, next)
}

object Vertex {
  def apply(label: String): Vertex = new Vertex(label)
}
