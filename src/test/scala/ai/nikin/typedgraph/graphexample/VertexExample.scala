package ai.nikin.typedgraph.graphexample

import ai.nikin.typedgraph.core.Vertex
import ai.nikin.typedgraph.graphexample.Content.Content

case class VertexExample[_IN <: Content, _OUT <: Content](n: String)
    extends Vertex[VertexExample[_IN, _OUT]](n) {
  override type IN  = _IN
  override type OUT = _OUT
}
