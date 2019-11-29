package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._

class IssueYearRangeObjectSpec extends FlatSpec {


  val rdf = XML.loadFile("jvm/src/test/resources/ocre_sample.rdf")

  "The IssueYearRange object" should "extract IssueYearRanges from OCRE RDF " in {
    val typeData = rdf \\ "TypeSeriesItem"
    val dateRanges = IssueYearRange.datesVector(typeData.toVector)
    val expectedDates = 4
    assert(dateRanges.size == expectedDates)
  }

}
