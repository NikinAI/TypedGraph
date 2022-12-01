package ai.nikin.typedgraph.graphexample

object Content {
  sealed trait Content
  case class ContentA() extends Content
  case class ContentB() extends Content
  case class ContentC() extends Content
  case class ContentD() extends Content
}
