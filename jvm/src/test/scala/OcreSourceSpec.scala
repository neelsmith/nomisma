package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._

class OcreSourceSpec extends FlatSpec {



  "The OcreSource object" should "be able to create an Ocre from source data in CEX Format" in  {
    val fName = "cex/ocre-valid.cex"
    val ocre = OcreSource.fromFile(fName)
    assert(ocre.size > 50000)
  }

  it should "build an Ocre from a file of RDF data" in {
    val fName = "jvm/src/test/resources/ocre_sample.rdf"
    val messageFrequency = 1
    val ocre = OcreSource.fromRdfFile(fName, messageFrequency)
    val expectedIssues = 4
    assert(ocre.size == expectedIssues)
  }

  it should "retrieve RDF data from a URL and build an Ocre" in {
      val url = "https://raw.githubusercontent.com/neelsmith/nomisma/master/jvm/src/test/resources/ocre_sample.rdf"
      val messageFrequency = 1
      val ocre = OcreSource.fromRdfUrl(url, messageFrequency)
      val expectedIssues = 4
      assert(ocre.size == expectedIssues)
  }

  it should "retrieve CEX data from a URL and build an OCRE" in {
    val url = "https://raw.githubusercontent.com/neelsmith/nomisma/master/cex/ocre-valid.cex"
    val ocre = OcreSource.fromUrl(url)
    println(ocre.size)
  }



}
