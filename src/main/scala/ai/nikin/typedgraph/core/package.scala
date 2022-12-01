package ai.nikin.typedgraph

package object core {
  type AnyVertex                                                               = TypelessVertex
  type AnyEdge                                                                 = TypelessEdge
  type AnyPath                                                                 = TypelessPath
  type VertexTO[FROM <: Vertex[FROM], TO <: Vertex[TO] { type IN = FROM#OUT }] =
    Vertex[TO] { type IN = FROM#OUT }
}
