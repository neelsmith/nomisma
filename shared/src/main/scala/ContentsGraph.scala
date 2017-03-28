package edu.holycross.shot.nomisma

//import com.esri.core.geometry._
import scala.scalajs.js
import js.annotation.JSExport



/** A hoard viewed as a geographic location
* with list of associated geographically located mints.
*
* @param id ID of the hoard.
* @param hoard Geographic point for the find spot.
* @param mintPoints Geographic points for mints represented
* in the hoard.
*/
@JSExport case class ContentsGraph (
  id: String,
  hoard: Point,
  mintPoints: Vector[MintPoint]
) {



  def mintsHtml = {
    val wrapped = mintPoints.map {
      pt => "<li><a href='" + urlFromId(pt.mint) + "'>" + prettyId(pt.mint) + "</a></li>"
    }
    "<ul>" + wrapped.mkString("\n") + "</ul>"
  }

  /** Create KML string for this hoard's
  * relations to mints represented in the hoard.
  */
  def kmlRelations: String = {
    val referencePoint = raw"""
    <Placemark>
    <name>${id}</name>
    <description><p>Hoard ${id}</p>
    ${mintsHtml}
    </description>
    <styleUrl>#hoard_style</styleUrl>
    <Point>
      <coordinates>${hoard},0</coordinates>
    </Point>
    </Placemark>
    """
    var mintStrings = scala.collection.mutable.ArrayBuffer[String]()
    for (m <- mintPoints ) {
      mintStrings += kmlLine("${id} - ${m.mint}", hoard.toString, m.pt.toString)
      mintStrings += m.toKml

    }
    referencePoint + "\n\n" + mintStrings.mkString("\n\n")
  }
}
