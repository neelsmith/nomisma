package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.io.Source

class TestCexData extends FlatSpec {

  "The composite CEX file" should "be parseable as 15 columns" in {
    val f = "jvm/src/test/resources/cex/ocre-composite-dates-portraits.cex"
    val issues = for (ln <- Source.fromFile(f).getLines.toVector.tail) yield {
      val cols = ln.split("#")
      if (cols.size >= 15) {
        val issue = OcreIssue(ln)
        Some(issue)
      } else {
        fail("FAILED ON " + ln + s"\nOnly found ${cols.size} columns")
        None
      }
    }
    val ocre = Ocre(issues.toVector.flatten)
    println("SIZE: " + ocre.size)
  }
}
