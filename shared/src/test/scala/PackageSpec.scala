package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
//import com.esri.core.geometry._

class PackageSpec extends FlatSpec {

  "The package object"  should  "recognize valid string values for a coin's side" in  {
    assert(coinSide("obverse").get == Obverse)
  }


}
