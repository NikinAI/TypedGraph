package ai.nikin.typedgraph.core

case class CanMakeEdge[
    FROM <: Vertex[FROM],
    EDGE[A <: Vertex[A], B <: Vertex[B]] <: Edge[A, EDGE, B],
    TO <: Vertex[TO],
](factory: EdgeFactory[EDGE])
