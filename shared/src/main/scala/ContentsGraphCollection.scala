package edu.holycross.shot.nomisma


import scala.collection.mutable.ArrayBuffer

import scala.scalajs.js.annotation._

/** Collection of hoards viewed as graph of find spots
* to mint locations.
*
* @param hoards Hoards viewed as [[ContentsGraph]] objects.
*/
@JSExportTopLevel("ContentsGraphCollection")
case class ContentsGraphCollection (hoards: Vector[ContentsGraph]) {

  val hoardIcon = "http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png"
  val mintIcon = "http://maps.google.com/mapfiles/kml/shapes/capital_small.png"

  def hoardsKml  = {
    var rslt = scala.collection.mutable.ArrayBuffer[String]()
    for (h <- hoards) {
      rslt += h.kmlRelations
    }
    rslt.mkString("\n\n")
  }

  /** Convert collection of hoards to KML
  * map linking find spots to mints.
  */
  def toKml: String = {
    preface + hoardsKml + trail
  }


  /** Opening of KML document.
  */
  val preface = raw"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
  <kml xmlns="http://www.opengis.net/kml/2.2" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
  <Folder>
  <Style id="hoard_style">
            <IconStyle>
                <Icon>
                    <href>${hoardIcon}</href>
                </Icon>
            </IconStyle>
        </Style>
        <Style id="mint_style">
                  <IconStyle>
                      <Icon>
                          <href>${mintIcon}</href>
                      </Icon>
                  </IconStyle>
              </Style>
"""

  /** Closing elements of KML document.
  */
  val trail = "</Folder>\n</kml>"

}
