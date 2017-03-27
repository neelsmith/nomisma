package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
//import com.esri.core.geometry._

class MintPointSpec extends FlatSpec {

  "A MintPoint"  should "have geometric coordinates" in {
    val mintPoint = MintPoint("athens", Point(23,26))
    assert (mintPoint.pt == Point(23,26))
  }


}
