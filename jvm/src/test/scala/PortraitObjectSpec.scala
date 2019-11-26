package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._
import java.net.URL

class PortraitObjectSpec extends FlatSpec {

  val rdf = XML.loadFile("jvm/src/test/resources/ocre_sample.rdf")


  "The Portrait object" should "extract Portraits from OCRE RDF " in {
    val descrs = rdf \\ "Description"
    val dv = descrs.toVector

    val portraitElems = dv.filter( d => (d  \\ "hasPortrait").size > 0 )
    val portraits = Portrait.portraitVector(portraitElems)

    val totalExpected = 6
    assert(portraits.size == totalExpected)
  }
}
