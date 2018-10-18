import scala.xml._

val root = XML.load("bm-deities.rdf")

val entries = root \\ "Deity"

val ids = for (e <- entries) yield {
  e.attributes.value.toString
}
