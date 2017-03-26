package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec

class HoardCollectionSpec extends FlatSpec {

  "A HoardCollection"  should "support report the number of hoards" in {
    val pointDate = ClosingDate(-450)
    val hoard = Hoard("dummy hoard", "dummy",Some(pointDate),Vector("athens"),None)
    val hoardCollection = HoardCollection(Vector(hoard))
    assert(hoardCollection.size == 1)
  }

  it should "report the set of mints found in the collection" in {

      val pointDate = ClosingDate(-450)
      val hoard1 = Hoard("dummy hoard 1", "dummy1",Some(pointDate),Vector("athens", "samos"),None)

      val hoard2 = Hoard("dummy hoard 2", "dummy2",Some(pointDate),Vector("athens","chios","samos"),None)


      val hoardCollection = HoardCollection(Vector(hoard1,hoard2))
      assert(hoardCollection.size == 2)

      assert (hoardCollection.mintSet == Set("athens","chios","samos"))

  }

}
