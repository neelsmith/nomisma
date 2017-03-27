package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec


class HoardCollectionDateSpec extends FlatSpec {

  "A HoardCollection"  should  "compute the maximum and minimum for closing date point" in {
    val pointDate = ClosingDate(-450)
    val hoard1 = Hoard("dummy hoard 1", "dummy1",Some(pointDate),Vector("athens", "samos"),None)

    val rangeDate = ClosingDate(-450, -430)
    val hoard2 = Hoard("dummy hoard 2", "dummy2",Some(rangeDate),Vector("athens","chios","samos"),None)

    val hoardCollection = HoardCollection(Vector(hoard1,hoard2))


    println("hoard1 avg " + hoard1.pointAverage)
    println("Hoard2 avg " + hoard2.pointAverage)
    assert(hoardCollection.maxAvgDate == -440)
    assert(hoardCollection.minAvgDate == -450)
  }

  it should "filter a list of date averages" in {
    val pointDate = ClosingDate(-450)
    val hoard1 = Hoard("dummy hoard 1", "dummy1",Some(pointDate),Vector("athens", "samos"),None)

    val rangeDate = ClosingDate(-450, -430)
    val hoard2 = Hoard("dummy hoard 2", "dummy2",Some(pointDate),Vector("athens","chios","samos"),None)

    val hoardCollection = HoardCollection(Vector(hoard1,hoard2))
    assert (hoardCollection.dated.size == 2)
  }


  it should "find a vector of closing date values" in {
    val rangeDate = ClosingDate(-450, -430)
    println("RANGE DATE = " + rangeDate)
    println("IT HAS PTAVG " + rangeDate.pointAverage)
  }

  /*

    val pointDate = ClosingDate(-450)
    val hoard1 = Hoard("dummy hoard 1", "dummy1",Some(pointDate),Vector("athens", "samos"),None)

    val rangeDate = ClosingDate(-450, -430)
    val hoard2 = Hoard("dummy hoard 2", "dummy2",Some(pointDate),Vector("athens","chios","samos"),None)

    val hoardCollection = HoardCollection(Vector(hoard1,hoard2))
    println(hoardCollection.closingDateVector)

  }*/
  it should "compute the maximum and minimum for actual closing date values" in pending
  /*
  assert(hoardCollection.maxAvgDate == -430)
  assert(hoardCollection.minAvgDate == -450)
}*/

  it should "cluster hoards by year span" in pending

  it should "bin hoards in equal divisions by year" in pending


}
