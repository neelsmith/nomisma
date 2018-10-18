package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
//import com.esri.core.geometry._

class PackageSpec extends FlatSpec {

  "The package object"  should "pretty print ID values" in {
    assert (prettyId("athens") == "Athens")
    assert (prettyId("aetolian_league") == "Aetolian League")
  }

  it should "recognize valid string values for a coin's side" in  {
    assert(coinSide("obverse").get == Obverse)
  }

}
