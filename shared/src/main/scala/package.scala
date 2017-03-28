package edu.holycross.shot



/** A library for working with freely available numismatic
* data from `nomisma.org`.
*
* ==Overview==
* The two fundamental classes are the [[edu.holycross.shot.nomisma.Hoard]] and the `Issue` (not yet implemented). Collections of Hoards (a [[HoardCollection]]) can
* be constructed from RDF XML data in a local file, or (not
* yet impemented) directly from the `nomisma.org` site.
*
* The package object includes utilities for fomatting and working
* strings used as LOD identifiers.
*
*/
package object nomisma {
  import scala.xml._
  //import com.esri.core.geometry._

  /** Extract the unique ID value from a long
  * identifying URL.
  *
  * @param lod Linked Open Data URL.
  */
  def idFromUrl (lod: String) : String = {
    lod.replaceAll(".+/","").replaceAll("#.+","")
  }

  /** Create `nomisma.org` URL from an ID value.
  *
  * @param id ID value.
  */
  def urlFromId (id: String) : String = {
    "http://nomisma.org/id/" + id
  }

  /** Pretty print ID value.
  *
  * @param id ID value.
  */
  def prettyId(id: String) : String = {
    id.replaceAll("_"," ").split(' ').map(_.capitalize).mkString(" ")
  }


  /** Extract coordinate data from a `SpatialThing` node
  * and pair it with the hoard ID.
  *
  * @param n `SpatialThing` node
  */
  def spatialForNode(n: scala.xml.Node) = {
    var hoardKey = ""
    val attV = n.attributes.toVector
    for (a <- attV) {
      if (a.key == "about") {
        hoardKey = idFromUrl( a.value.text)
      } else {}
    }
    val lat = n \ "lat"
    val lon = n \ "long"
    (hoardKey,Point(lon.text.toDouble,lat.text.toDouble))
  }


  /** Create KML string for line linking two points.
  *
  * @param label Human-readable label for line.
  * @param pr1 String representation of an x,y pair.
  * @param pr2 String representation of second x,y pair.
  */
  def kmlLine(label: String, pr1: String, pr2: String): String = {
    val lineString = raw"""
    <Placemark>
        <name>${label}</name>
        <description></description>
        <LineString>
            <extrude>1</extrude>
            <tessellate>1</tessellate>
            <coordinates>${pr1} ${pr2}</coordinates>
        </LineString>
    </Placemark>
    """"
    lineString
  }
}
