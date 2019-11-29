package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._

class LegendSpec extends FlatSpec {




  "A Legend"  should "have a CoinSide" in {
    val legend = Legend("coinId", Obverse, "EX SC")
    assert (legend.side == Obverse)
  }

  "The Legend object"  should "create a Vector of Legends from a Vector of RDF description nodes" in  {
    val root = XML.load("jvm/src/test/resources/ocre_sample.rdf")
    val descrs = root \\ "Description"
    val legends = Legend.legendVector(descrs.toVector)
  }

}
