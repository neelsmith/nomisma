package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._

class OcreObjectSpec extends FlatSpec {
  val rdf = XML.loadFile("jvm/src/test/resources/ocre_sample.rdf")

  "The Ocre object" should "parse OCRE from RDF source" in {
    val ocre = Ocre.parseRdf(rdf)
    val expectedIssues = 4
    assert(ocre.issues.size == expectedIssues)

    val expectedLegends = 8
    assert(ocre.legends.size == expectedLegends)
  }
}
