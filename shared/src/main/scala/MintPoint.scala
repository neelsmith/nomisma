package edu.holycross.shot.nomisma

import scala.scalajs.js
import js.annotation.JSExport

/** A geographically located mint.
*
* @param mint Identifier for mint.
* @param pt Geographic location of mint.
*/
@JSExport case class MintPoint(mint: String, pt: Point) {


  /** Representation of the mint point as a KML placemark.
  */
  def toKml: String = {
    raw"""<Placemark>
    <name>${mint}</name>
    <description><p>Mint ${mint}</p>
    </description>
    <styleUrl>#mint_style</styleUrl>
    <Point>
      <coordinates>${pt},0</coordinates>
    </Point>
    </Placemark>"""
  }
}
