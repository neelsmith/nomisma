package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec

class HoardSourceSpec extends FlatSpec {

  "A HoardSource"  should "instantiate a HoardCollection from RDF XML" in {
    val srcFile = "jvm/src/test/resources/igch.rdf"
    val hoards = HoardSource.fromFile(srcFile)
    assert(hoards.size == 2387)
  }

}
