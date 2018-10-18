package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._

class LegendSpec extends FlatSpec {


  val rdf = XML.loadFile("shared/src/test/resources/ocre_sample.rdf")


  "A Legend"  should "have a CoinSide" in {
    val legend = Legend("coinId", Obverse, "EX SC")
    assert (legend.side == Obverse)
  }

  "The Legend object" should "extract Legends from OCRE RDF " in {
    val descrs = rdf \\ "Description"
    val dv = descrs.toVector
    val legends = Legend.legendVector(dv)
    val totalExpected = 8
    assert(legends.size == totalExpected)

    val expectedPerSide = 4
    assert(legends.filter(_.side == Obverse).size == expectedPerSide)

    assert(legends.filter(_.side == Reverse).size == expectedPerSide)
  }

}
