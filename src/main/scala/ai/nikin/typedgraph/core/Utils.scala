package ai.nikin.typedgraph.core

import scala.collection.immutable.ListSet

object Utils {
  implicit class SeqExtra[A](l: Seq[A]) {
    def toListSet: ListSet[A] = ListSet() ++ l
  }

}
