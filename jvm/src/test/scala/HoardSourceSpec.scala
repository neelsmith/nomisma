package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec

class HoardSourceSpec extends FlatSpec {

  "A HoardSource"  should "instantiate a HoardCollection from RDF XML" in {
    val srcFile = "jvm/src/test/resources/singlehoard.rdf"
    val hoards = HoardSource.fromFile(srcFile)
    assert(hoards.size == 1)
  }

  it should "be able read all of IGCH" in {
    val srcFile = "jvm/src/test/resources/igch.rdf"
    val hoards = HoardSource.fromFile(srcFile)
    assert(hoards.size == 2387)
  }

  it should "find some valid structures" in pending /*{
    val srcFile = "jvm/src/test/resources/igch.rdf"
    val hoards = HoardSource.fromFile(srcFile)
    println(hoards.hoards(0).prettyPrint)
  }
*/
}
