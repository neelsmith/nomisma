package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import edu.holycross.shot.histoutils._


class GenericIssuesCatalogSpec extends FlatSpec {


  val genericOcre = GenericIssuesCatalogSource.fromFile("jvm/src/test/resources/ocre-sample10.cex")
  val genericCrro = GenericIssuesCatalogSource.fromFile("jvm/src/test/resources/crro-sample10.cex")

  "A GenericIssuesCatalog" should "create a Histogram for a given property" in  {
    val metalHisto : Histogram[String] = genericOcre.histogram("material")
    val expectedMetals = 3
    assert(metalHisto.size == expectedMetals)
  }

  it should "create an empty Histogram with a warning if the property name is bad" in {
    val bogusHisto: Histogram[String] = genericOcre.histogram("bad property name")
    assert(bogusHisto.size == 0)
  }

  it should "be able to add together two catalogs" in {
    val composite =  genericOcre ++ genericCrro
    assert(composite.size == (genericOcre.size + genericCrro.size))
  }
}
