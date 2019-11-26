package edu.holycross.shot.nomisma
import edu.holycross.shot.cite._
//import java.net.URL

import scala.scalajs.js.annotation._

@JSExportTopLevel("OcreIssue2")
case class OcreIssue2(
    basics: BasicIssue,
    otype: Option[TypeDescription],
    rtype: Option[TypeDescription],
    olegend: Option[Legend],
    rlegend: Option[Legend],
    oportrait: Option[Portrait],
    rportrait: Option[Portrait],
    mintGeo: Option[MintPoint]
  ) extends NomismaEntity {

  def urlString = {
    "http:nomisma.org/id/" + basics.id
  }

  /*def url = {
    new URL(urlString)
  }
*/
  def urn: Cite2Urn = {
    Cite2Urn("urn:cite2:nomisma:ocre.hc:" + basics.id)
  }
  def label = basics.labelText


  def kml: String = {
    mintGeo match {
      case None => ""
      case _ => mintGeo.get.toKml
    }
  }



}
