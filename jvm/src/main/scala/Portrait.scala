package edu.holycross.shot.nomisma

import edu.holycross.shot.cite._
import scala.scalajs.js.annotation._

/**  Legend for a single side of a coin.
*
* @param coin
* @param side
* @param legend
*/
@JSExportTopLevel("Portrait")
case class Portrait (coin: String, side: CoinSide, portrait: String) extends NomismaEntity {

  def urlString = portrait.toString
  def label = portrait.toString.replaceAll("http://collection.britishmuseum.org/id/person-institution/", "").replaceAll("http://nomisma.org/id/", "")

  def objectId : String =  portrait.toString.replaceAll("http://nomisma.org/id/", "")
  def urn = {
    Cite2Urn("urn:cite2:nomisma:portrait.hc:" + objectId)
  }

}


object Portrait {

  def sideForString(s: String): CoinSide = {
    s.toLowerCase match {
      case "reverse" => Reverse
      case "obverse" => Obverse
      case sideLabel: String => throw new Exception("Unrecognized value for coin side: " + sideLabel)
    }
  }
  /** Given a Vector of OCRE Description nodes,
  * return a Vector of [[Portrait]]s.
  *
  * @param descriptionV Vector of OCRE Description nodes.
  */
  def portraitVector(descriptionV: Vector[scala.xml.Node]) : Vector[Portrait] = {
    val portraits = for (p <- descriptionV) yield {
      val rdgs = p \\ "hasPortrait"
      // p.attributes.value.toString is BOTH
      // coin ID and side!
      if (rdgs.nonEmpty) {
        val coinSide = p.attributes.value.toString.split("#")
        //println("COIN SIDE " + coinSide.toVector)
        //println("ATTRS "  + rdgs(0).attributes)
        Some(Portrait(coinSide(0), sideForString(coinSide(1)), rdgs(0).attributes.toString))//value ))

      } else {
        None
      }
    }
    portraits.flatten
  }

  def portraitVectorHosed(descriptionV: Vector[scala.xml.Node]) : Vector[Portrait] = {
    val portraits = for (p <- descriptionV) yield {
      val rdgs = (p \\ "hasPortrait").toVector
      // p.attributes.value.toString is BOTH
      // coin ID and side
      if (rdgs.nonEmpty) {
        val coinParts = p.attributes.value.toString.split("#")
        val portraitUrl = rdgs(0).attributes.toVector(0).value.toString

        val side = coinSide(coinParts(1))
        side match {
          case None => None
          case _  =>   Some(Portrait(
            coinParts(0),
            side.get,
            portraitUrl ))
        }

      } else {
        None
      }
    }
    portraits.flatten
  /*
    val portraitsRaw = for (p <- portraitText.flatten) yield {
      val triple = p.split("#")
      if (triple.size == 3) {
        Some(Portrait(triple(0), triple(1), triple(2))  )
      } else {
        println("Struck out on " + p)
        None
      }
    }
    portraitsRaw.filter(_.isDefined).map(_.get)
    */
  }
}
