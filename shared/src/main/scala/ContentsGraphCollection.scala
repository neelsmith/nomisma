package edu.holycross.shot.nomisma


import scala.collection.mutable.ArrayBuffer

import scala.scalajs.js
import js.annotation.JSExport

/** Collection of hoards viewed as graph of find spots
* to mint locations.
*
* @param hoards Hoards viewed as [[ContentsGraph]] objects.
*/
@JSExport case class ContentsGraphCollection (hoards: Vector[ContentsGraph]) {

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
  val preface = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
  <kml xmlns="http://www.opengis.net/kml/2.2" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
  <Folder>
"""

  /** Closing elements of KML document.
  */
  val trail = "</Folder>\n</kml>"

}
