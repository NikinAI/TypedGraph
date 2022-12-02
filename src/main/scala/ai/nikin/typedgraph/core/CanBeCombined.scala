package ai.nikin.typedgraph.core

import scala.annotation.implicitNotFound

@implicitNotFound(
    """Combining
    ${A}
and
    ${B}
is not allowed with your current setup!

To enable this combination, add:

----
    implicit val ev = new CanBeCombined[${A}, ${B}, ${OUTPUT}] {
      def apply(a: ${A}, b: ${B}): ${OUTPUT} = ???
    }
----

In the scope of:
  """)
abstract class CanBeCombined[
    A <: Vertex[A],
    B <: Vertex[B],
    OUTPUT <: VertexCombiner[OUTPUT],
] {
  def apply(a: A, b: B): OUTPUT
}
