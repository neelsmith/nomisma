package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._


class TypeDescriptionObjectSpec extends FlatSpec {


  val rdf = XML.loadFile("jvm/src/test/resources/ocre_sample.rdf")

  "The TypeDescription object" should "extract TypeDescriptions from OCRE RDF " in {
    val descrs = rdf \\ "Description"
    val dv = descrs.toVector

    val typeDescrNodes = dv.filter( d => (d  \\ "description").size > 0 )
    val typeDescriptions =  TypeDescription.typeDescriptionVector(typeDescrNodes)


    val totalExpected = 8
    assert(typeDescriptions.size == totalExpected)

    val expectedPerSide = 4
    assert(typeDescriptions.filter(_.side == Obverse).size == expectedPerSide)

    assert(typeDescriptions.filter(_.side == Reverse).size == expectedPerSide)
  }
}
