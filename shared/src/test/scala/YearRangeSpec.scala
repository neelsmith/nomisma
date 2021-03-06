package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec

class YearRangeSpec extends FlatSpec {

  "A YearRangeSpec"  should "support constructor with a single integer value" in {
    val pointDate = YearRange(-450)
    assert(pointDate.year1 == -450)
    pointDate.year2 match {
      case None => assert(true)
      case _ => fail("Second date value should have been None")
    }
  }


  it should "support constructor with two integer values" in {
    val rangeDate = YearRange(-450,-430)
    assert(rangeDate.year1 == -450)
    rangeDate.year2 match {
      case None => fail("Should have found second range date")
      case _ => assert(rangeDate.year2.get == -430)
    }
  }

  it should "require second date to be more recent than first" in {
    try {
      val rangeDate = YearRange(-430,-440)
      fail("Should have enforced sequence earlier->later")
    } catch {
      case e: IllegalArgumentException => assert(e.getMessage() == "requirement failed: Date range must be expressed in order earlier to later, not -430 to -440")
      case _ : Throwable => fail("Expected IllegalArgumentException")
    }
  }

  it should "provide an average point represenation of closing dates" in {
    val pointDate = YearRange(-450)
    val rangeDate = YearRange(-450,-430)

    assert(pointDate.pointAverage == -450)
    assert(rangeDate.pointAverage == -440)
  }

  it should "override the default toString function" in {
    val pointDate = YearRange(-450)
    val rangeDate = YearRange(-450,-430)

    assert(pointDate.toString == "450 BCE")
    assert(rangeDate.toString == "450 BCE:430 BCE")
  }

  it should "support providing a separating string for date ranges" in {
      val rangeDate = YearRange(-14,27)
      println(rangeDate.toString(" - "))
  }

  it should "have a contains function" in {
    val pointDate = YearRange(-450)
    assert(pointDate.contains(-450))
    assert(pointDate.contains(-451) == false)

    val rangeDate = YearRange(-450,-430)
    assert(rangeDate.contains(-440))
    assert(rangeDate.contains(-451) == false)

  }

  "The YearRange object" should "have format single Ints as year Strings" in {
    val yr0 = YearRange.yearString(0)
    val expected0 = "0 (?)"
    assert (yr0 == expected0)

    val ce = YearRange.yearString(117)
    val expectedCe = "117 CE"
    assert(ce == expectedCe)

    val bce = YearRange.yearString(-14)
    val expectedBce = "14 BCE"
    assert(bce == expectedBce)
  }


}
