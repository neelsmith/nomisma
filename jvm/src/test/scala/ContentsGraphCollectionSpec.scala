package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec



class ContentsGraphCollectionSpec extends FlatSpec {

  "A ContentsGraphCollection"  should "" in {
    val coll = HoardSource.fromFile("jvm/src/test/resources/singlehoard.rdf")
    val cgcoll = HoardSource.contentsGraph(coll)
    println(cgcoll.hoards(0))
  }


}
