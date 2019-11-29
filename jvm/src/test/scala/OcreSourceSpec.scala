package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._

class OcreSourceSpec extends FlatSpec {

  val fName = "cex/ocre-valid.cex"



  "The OcreSource object" should "be able to create an Ocre from source data in CEX Format" in  {
    val ocre = OcreSource.fromFile(fName)
  }


}
