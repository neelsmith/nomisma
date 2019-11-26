package edu.holycross.shot.nomisma

import scala.scalajs.js.annotation._

/** A geographically located mint.
*
* @param mint Identifier for mint.
* @param pt Geographic location of mint.
*/
@JSExportTopLevel("MintPoint")
case class MintPoint(mint: String, pt: Point) {


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

object MintPoint {

  def apply(cex: String) : MintPoint = {
    def cols = cex.split("#")
    if (cols.size != 3) {
      throw new Exception("Wrong number of columns to parse a MintPoint from " +  cex)
    } else {
      try {
        val id = cols(0)
        val x = cols(1).toDouble
        val y = cols(2).toDouble
        MintPoint(id,Point(x,y))
      } catch {

        case t: Throwable => {
          println("MintPoint: unable to parse point from " + cex)
          throw t
        }
      }
    }
  }
}
