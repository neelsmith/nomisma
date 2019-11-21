package edu.holycross.shot.nomisma
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
  def label = basics.labelText
}
