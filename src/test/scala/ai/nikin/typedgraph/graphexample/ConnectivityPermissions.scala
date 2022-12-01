package ai.nikin.typedgraph.graphexample

import ai.nikin.typedgraph.core._

object ConnectivityPermissions {
  object AllVertexToEdge {
    implicit def canMakeEdgeExample[A <: Vertex[A], B <: Vertex[B]]: CanMakeEdge[A, EdgeExample, B] =
      CanMakeEdge[A, EdgeExample, B](EdgeExample)
  }
}
