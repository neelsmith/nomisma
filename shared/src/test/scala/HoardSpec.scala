package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
//import com.esri.core.geometry._

class HoardSpec extends FlatSpec {

  "A Hoard"  should "support have a pretty print method" in {
    val pointDate = YearRange(-450)
    val hoard = Hoard("dummy hoard", "dummy",Some(pointDate),Vector("athens"),None)
    hoard.prettyPrint
  }

  it should "support geometric point locations" in {
    val pointDate = YearRange(-450)
    val hoard = Hoard("dummy hoard", "dummy",Some(pointDate),Vector("athens"),Some(new Point(39.215,44)))
    hoard.geo match {
      case pt: Some[Point] => {
        assert(pt.get.x == 39.215)
        assert(pt.get.y == 44)
      }

      case _ => fail("Expected to find a point here.")
    }
  }

  it should "format a record as CSV" in {
      val pointDate = YearRange(-450)
      val hoard = Hoard("dummy hoard", "dummy",Some(pointDate),Vector("athens"),Some(new Point(39.215,44)))
      println("CSV IS " + hoard.csv)
  }

  it should "export a record to KML" in pending/*{

    val pointDate = YearRange(-450)
    val hoard = Hoard("dummy hoard", "dummy",Some(pointDate),Vector("athens","chios"),Some(new Point(39.215,44)))
    val kml = hoard.kmlPoint
  }*/

}
