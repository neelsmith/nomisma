package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._

class OcreSpec extends FlatSpec {
  val rdf = XML.loadFile("shared/src/test/resources/ocre_sample.rdf")

  "The Ocre object" should "parse OCRE from RDF source" in {
    assert(1 > 0)
  }
}
