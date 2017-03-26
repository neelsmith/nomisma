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

  it should "fold in geographic coordinates insanely separated from hoard in RDF XML representation" in {
    val srcFile = "jvm/src/test/resources/singlehoard.rdf"
    val coll = HoardSource.fromFile(srcFile)
    val hoard1 = coll.hoards(0)
    assert (hoard1.geo.get == "39.215,44")
  }

  it should "be able to retrieve spatial data for a set of mints" in {
    //val mints = Set("athens","thessalian_league","thasos")
    val mints = Set("athens")
    val geo = HoardSource.geoForMints(mints)
    println(geo)
  }

}
