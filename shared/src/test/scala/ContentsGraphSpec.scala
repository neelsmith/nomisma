package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import com.esri.core.geometry._

class ContentsGraphSpec extends FlatSpec {

  "A Contents Graph"  should "support map mints to geographic coodinates" in {
    val pointDate = ClosingDate(-450)
    val hoard = Hoard("dummy hoard", "dummy",Some(pointDate),Vector("athens"),Some(new Point(23,26)))
    hoard.prettyPrint

    
  }


}
