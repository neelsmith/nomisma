package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._

class OcreObjectSpec extends FlatSpec {
  val root = XML.loadFile("jvm/src/test/resources/ocre_sample.rdf")

  "The NomismaRdfCollection object" should "parse OCRE2 from RDF source" in {
    val ocreRdf = NomismaRdfCollection.parseRdf(root)
    val expectedIssues = 4
    assert(ocreRdf.issues.size == expectedIssues)

    val expectedLegends = 8
    assert(ocreRdf.legends.size == expectedLegends)
  }

  it should "construct an Ocre" in {
    val ocreRdf = NomismaRdfCollection.parseRdf(root)
    val ocre = ocreRdf.toOcre(msgPoint = 1)
  }
}
