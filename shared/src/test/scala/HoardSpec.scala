package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec

class HoardSpec extends FlatSpec {

  "A Hoard"  should "support have a pretty print method" in {
    val pointDate = ClosingDate(-450)
    val hoard = Hoard("dummy hoard", "dummy",Some(pointDate),Vector("athens"),None)
    hoard.prettyPrint
  }

}
