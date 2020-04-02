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
    val expectedPoint =  Point(39.215,44)
    assert (hoard1.geo.get == expectedPoint)
  }

  it should "be able to retrieve spatial data for a mint" in {
    val mints = Set("athens")
    val geo = HoardSource.geoForMints(mints)

    assert(geo.size == 1)
    val actualEntry = geo.mintPoints(0)
    assert (actualEntry.mint == "athens")
    assert(actualEntry.pt ==  Point(23.7225,37.974722))
  }

  it should "be able to retrieve spatial data for multiple mints" in  {
    val mints = Set("athens","thasos", "corinth")

    val geo = HoardSource.geoForMints(mints)

    print("HERE's geo " + geo.mintPoints.mkString("\n") )
    assert(geo.size == 3)
    //val athensPoint = geo get "athens"
    //val expected = new Point(23.7225,37.974722)
  //  assert(expected == athensPoint.get)
  }

  it should "map hoard contents to locations of mints" in {
    val srcFile = "jvm/src/test/resources/singlehoard.rdf"
    val hoards = HoardSource.fromFile(srcFile)
    val cg = HoardSource.contentsGraph(hoards)
    println(cg)
  }


}
