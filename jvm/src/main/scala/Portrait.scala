package edu.holycross.shot.nomisma

import edu.holycross.shot.cite._
import scala.scalajs.js.annotation._


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


/**  Legend for a single side of a coin.
*
* @param coin
* @param side
* @param portrait
*/
@JSExportTopLevel("Portrait")
case class Portrait (coin: String, side: CoinSide, portrait: String) extends  NomismaEntity  {

  def urlString = portrait.toString
  def label = portrait.toString.replaceAll("http://collection.britishmuseum.org/id/person-institution/", "").replaceAll("http://nomisma.org/id/", "")

  def objectId : String =  ricIdFromUrl(portrait.toString)
    //portrait.toString.replaceAll("http://nomisma.org/id/", "")
  def urn = {
    Cite2Urn("urn:cite2:nomisma:portrait.hc:" + objectId)
  }

}


object Portrait extends LogSupport {
  Logger.setDefaultLogLevel(LogLevel.INFO)
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
    info("Parsing " + descriptionV.size + " RDF description elements.")
    val portraits = for (p <- descriptionV) yield {
      val rdgs = p \\ "hasPortrait"
      // p.attributes.value.toString is BOTH
      // coin ID and side!
      if (rdgs.nonEmpty) {
        val coinSide = p.attributes.value.toString.split("#")
        val id = ricIdFromUrl(coinSide(0))

        debug(s"Portrait coin side for ${id}: " + coinSide.toVector)
        // test if readings has attribute; use value of readings if none
        debug(s"Portrait attributes for ${id}: "  + rdgs(0).attributes)
        val portraitId = if (rdgs(0).attributes.isEmpty){
          rdgs(0).text.toString.trim
        } else {
          rdgs(0).attributes.value.toString.trim
        }

        Some(Portrait(id, sideForString(coinSide(1)),portraitId ))

      } else {
        None
      }
    }
    val results = portraits.flatten
    info("Extracted " + results.size + " Portrait objects.")
    results
  }


}
