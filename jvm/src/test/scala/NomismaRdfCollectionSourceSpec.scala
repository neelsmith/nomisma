package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._

class NomismaRdfCollectionSourceSpec extends FlatSpec {
  val fName = "jvm/src/test/resources/ocre_sample.rdf"

  "The NomismaRdfCollectionSource object" should "be able to create an OcreRdf from a file" in  {
    val ocreRdf = NomismaRdfCollectionSource.fromFile(fName)

  }


}
