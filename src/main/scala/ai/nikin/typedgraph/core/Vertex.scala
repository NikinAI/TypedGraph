package ai.nikin.typedgraph.core

class Vertex(val label: String) {}

object Vertex {
  def apply(label: String): Vertex = new Vertex(label)
}
