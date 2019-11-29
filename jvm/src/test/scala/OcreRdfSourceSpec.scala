package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._

class OcreRdfSourceSpec extends FlatSpec {
  val fName = "jvm/src/test/resources/ocre_sample.rdf"
    //val root = XML.loadFile(fName)


  "The OcreRdfSource object" should "be able to create an OcreRdf from a file" in  {
    val ocreRdf = OcreRdfSource.fromFile(fName)
  }


}
