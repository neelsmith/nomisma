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


  /** Create KML string for this hoard's
  * relations to mints represented in the hoard.
  */
  def kmlRelations: String = {
    val referencePoint = raw"""
    <Placemark>
    <name>${id}</name>
    <description>""" +
    s"""${id}</description>
    <Point>
      <coordinates>${hoard},0</coordinates>
    </Point>
    </Placemark>
    """
    var lineStrings = scala.collection.mutable.ArrayBuffer[String]()
    for (m <- mintPoints ) {
      lineStrings += kmlLine("${id} - ${m.mint}", hoard.toString, m.pt.toString)
    }
    referencePoint + "\n\n" + lineStrings.mkString("\n\n")
  }
}
