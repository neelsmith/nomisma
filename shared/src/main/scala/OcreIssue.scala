package edu.holycross.shot.nomisma
import edu.holycross.shot.cite._
import java.net.URL
import scala.scalajs.js
import js.annotation.JSExport



@JSExport  case class OcreIssue(
    basics: BasicIssue,
    otype: Option[TypeDescription],
    rtype: Option[TypeDescription],
    olegend: Option[Legend],
    rlegend: Option[Legend],
    oportrait: Option[Portrait],
    rportrait: Option[Portrait],
    mintGeo: Option[MintPoint]
  ) extends NomismaEntity {

  def url = {
    new URL("http:nomisma.org/id/" + basics.id)
  }

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
