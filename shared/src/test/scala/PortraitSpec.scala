package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._
import java.net.URL

class PortraitSpec extends FlatSpec {

  //val rdf = XML.loadFile("shared/src/test/resources/ocre_sample.rdf")
  val augustus = new URL("http://nomisma.org/id/augustus")

  "A Portrait"  should "have a CoinSide" in {
    val portrait = Portrait("coinId", Obverse, augustus)
    assert (portrait.side == Obverse)
  }

  it should "have a label function" in {
    val portrait = Portrait("coinId", Obverse, augustus)
    val expected = "augustus"
    assert(portrait.label ==  expected)
  }

  it should "have a url function" in {
    val portrait = Portrait("coinId", Obverse, augustus)
    val expected = augustus
    assert(portrait.url == augustus)
  }

/*
  "The Portrait object" should "extract Portraits from OCRE RDF " in {
    val descrs = rdf \\ "Description"
    val dv = descrs.toVector

    val portraitElems = dv.filter( d => (d  \\ "hasPortrait").size > 0 )
    val portraits = Portrait.portraitVector(portraitElems)

    val totalExpected = 6
    assert(portraits.size == totalExpected)
  }*/
}
