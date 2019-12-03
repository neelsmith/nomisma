package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import edu.holycross.shot.histoutils._


class GenericIssuesCatalogSpec extends FlatSpec {


  val ocre = OcreSource.fromFile("jvm/src/test/resources/ocre-sample10.cex")

  "An Ocre" should "create a Histogram for a given property" in  {
    val metalHisto : Histogram[String] = ocre.histogram("material")
    val expectedMetals = 3
    assert(metalHisto.size == expectedMetals)
  }

  it should "create an empty Histogram with a warning if the property name is bad" in {
    val bogusHisto: Histogram[String] = ocre.histogram("bad property name")
    assert(bogusHisto.size == 0)
  }
}
