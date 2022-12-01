package ai.nikin.typedgraph.graphexample

import ai.nikin.typedgraph.core.Vertex
import ai.nikin.typedgraph.graphexample.Content.Content

case class SecondVertex[CONTENT <: Content](n: String) extends Vertex[SecondVertex[CONTENT]](n) {
  override type IN  = CONTENT
  override type OUT = CONTENT
}
