package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._

class LegendSpec extends FlatSpec {




  "A Legend"  should "have a CoinSide" in {
    val legend = Legend("coinId", Obverse, "EX SC")
    assert (legend.side == Obverse)
  }

}
