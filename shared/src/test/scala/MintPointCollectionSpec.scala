package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
//import com.esri.core.geometry._

class MintPointCollectionSpec extends FlatSpec {

val csv = """mint,lon,lat
aachen,6.083333,50.783333
abacaenum,15.102253,38.0505243
abbaetae-mysi,28.75,39.25
abdera_hispania,-3.016667,36.733333
"""

  "The MintPointCollection object"  should "build a MintPointCollection from csv source" in {
    val mintCollection = MintPointCollection(csv, ",")
    val expectedMints = 4
    assert(mintCollection.size  == expectedMints)
  }


}
