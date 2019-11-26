package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec
import scala.xml._
import java.net.URL

class PortraitSpec extends FlatSpec {

  
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

}
