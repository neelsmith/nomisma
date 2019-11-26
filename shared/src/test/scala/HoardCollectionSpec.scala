package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec


//import com.esri.core.geometry._

class HoardCollectionSpec extends FlatSpec {

  "A HoardCollection"  should "support report the number of hoards" in {
    val pointDate = YearRange(-450)
    val hoard = Hoard("dummy hoard", "dummy",Some(pointDate),Vector("athens"),None)
    val hoardCollection = HoardCollection(Vector(hoard))
    assert(hoardCollection.size == 1)
  }

  it should "report the set of mints found in the collection" in {
      val pointDate = YearRange(-450)
      val hoard1 = Hoard("dummy hoard 1", "dummy1",Some(pointDate),Vector("athens", "samos"),None)

      val hoard2 = Hoard("dummy hoard 2", "dummy2",Some(pointDate),Vector("athens","chios","samos"),None)

      val hoardCollection = HoardCollection(Vector(hoard1,hoard2))
      assert(hoardCollection.size == 2)

      assert (hoardCollection.mintSet == Set("athens","chios","samos"))
  }

  it should "be able to create a collection containing only geographically located hoards" in  {
    val pointDate = YearRange(-450)
    val hoard1 = Hoard("dummy hoard 1", "dummy1",Some(pointDate),Vector("athens", "samos"),Some(new Point(23,36)))

    val hoard2 = Hoard("dummy hoard 2", "dummy2",Some(pointDate),Vector("athens","chios","samos"),None)

    val hoardCollection = HoardCollection(Vector(hoard1,hoard2))
    assert(hoardCollection.size == 2)
    assert(hoardCollection.located.size == 1)
  }

  it should "be able to create a collection containing only dated hoards" in {
    val pointDate = YearRange(-450)
    val hoard1 = Hoard("dummy hoard 1", "dummy1",Some(pointDate),Vector("athens", "samos"),Some(new Point(23,36)))

    val hoard2 = Hoard("dummy hoard 2", "dummy2",None,Vector("athens","chios","samos"),None)

    val hoardCollection = HoardCollection(Vector(hoard1,hoard2))
    assert(hoardCollection.dated.size == 1)
  }


  it should "create a KML string of the whole collection" in {
    val pointDate = YearRange(-450)
    val hoard1 = Hoard("dummy hoard 1", "dummy1",Some(pointDate),Vector("athens", "samos"),Some(new Point(23,36)))

    val hoard2 = Hoard("dummy hoard 2", "dummy2",Some(pointDate),Vector("athens","chios","samos"),Some(new Point(23.5,36)))

    val hoardCollection = HoardCollection(Vector(hoard1,hoard2))
    //println(hoardCollection.toKml)
  }

  it should "create a CSV string of the whole collection" in {
    val pointDate = YearRange(-450)
    val hoard1 = Hoard("dummy hoard", "dummy",Some(pointDate),Vector("athens"),Some(new Point(39.215,44)))
    val hoard2 = Hoard("dummy hoard 2", "dummy2",Some(pointDate),Vector("athens","chios","samos"),Some(new Point(23.5,36)))
    val hoardCollection = HoardCollection(Vector(hoard1,hoard2))

    println("CSV FOR ALL = " + hoardCollection.csv)
  }


  it should "create a cooccurrence matrix by mints" in pending

  it should "output a tabular one-hot view of hoard contents" in pending

}
