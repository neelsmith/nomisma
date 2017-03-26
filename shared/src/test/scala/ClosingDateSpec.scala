package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec

class ClosingDateSpec extends FlatSpec {

  "A ClosingDateSpec"  should "support constructor with a single integer value" in {
    val pointDate = ClosingDate(-450)
    assert(pointDate.d1 == -450)
    pointDate.d2 match {
      case None => assert(true)
      case _ => fail("Second date value should have been None")
    }
  }
  it should "support constructor with two integer values" in {
    val rangeDate = ClosingDate(-450,-430)
    assert(rangeDate.d1 == -450)
    rangeDate.d2 match {
      case None => fail("Should have found second range date")
      case _ => assert(rangeDate.d2.get == -430)
    }
  }

  it should "require second date to be more recent than first" in {
    try {
      val rangeDate = ClosingDate(-430,-440)
      fail("Should have enforced sequence earlier->later")
    } catch {
      case e: IllegalArgumentException => assert(e.getMessage() == "requirement failed: Date range must be expressed in order earlier to later, not -430 to -440")
      case _ : Throwable => fail("Expected IllegalArgumentException")
    }
  }

  it should "provide an average point represenation of closing dates" in {
    val pointDate = ClosingDate(-450)
    val rangeDate = ClosingDate(-450,-430)

    assert(pointDate.pointAverage == -450)
    assert(rangeDate.pointAverage == -440)
  }

  it should "override the default toString functin" in {
    val pointDate = ClosingDate(-450)
    val rangeDate = ClosingDate(-450,-430)

    assert(pointDate.toString == "-450")
    assert(rangeDate.toString == "-450:-430")
  }


}
