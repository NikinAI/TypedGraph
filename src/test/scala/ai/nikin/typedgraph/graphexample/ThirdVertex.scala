package ai.nikin.typedgraph.graphexample

import ai.nikin.typedgraph.core.Vertex

case class ThirdVertex[CONTENT](n: String) extends Vertex[ThirdVertex[CONTENT]](n) {
  override type IN  = CONTENT
  override type OUT = CONTENT
}
