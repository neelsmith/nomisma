package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec


class BasicIssueSpec extends FlatSpec {

  val cex = "1_2.aug.10#RIC I (second edition) Augustus 10#denarius#ar#augustus#emerita#lusitania"

  "A BasicIssue"  should "support constructor from CEX" in {
    val basicIssue = BasicIssue(cex)
    basicIssue match {
      case bi: BasicIssue => assert(true)
      case _ => fail("Should have constructed a basic issue")
    }
  }

  it should "have a bunch of string properties" in {
    val basicIssue = BasicIssue(cex)
    assert (basicIssue.id == "1_2.aug.10")
    assert(basicIssue.labelText == "RIC I (second edition) Augustus 10")
    assert(basicIssue.denomination == "denarius")
    assert(basicIssue.material == "ar")
    assert(basicIssue.authority == "augustus")
    assert(basicIssue.mint == "emerita")
    assert(basicIssue.region == "lusitania")

  }

}
