package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._

class OcreRdfSpec extends FlatSpec {
  val fName = "jvm/src/test/resources/ocre_sample.rdf"
    //val root = XML.loadFile(fName)


  "An OcreRdf" should "be able to create an Ocre" in  {
    val ocreRdf = OcreRdfSource.fromFile(fName)
    val ocre = ocreRdf.toOcre(msgPoint = 1)
  }

}
