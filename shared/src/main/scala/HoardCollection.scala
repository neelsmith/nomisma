package edu.holycross.shot.nomisma


/** A collection of [[Hoard]]s.
*
* @param hoards Vector of [[Hoard]]s in this collection.
*/
case class HoardCollection(hoards: Vector[Hoard])  {
  /** Preface to KML document.
  */
  val preface = """<?xml version="1.0" encoding="UTF-8"?>
  <kml xmlns="http://www.opengis.net/kml/2.2">
    <Document>
  """

  /** Conclusion to KML document.
  */
  val trail = "</Document></kml>"


  /** Number of hoards in the collection.
  */
  def size: Int = {
    hoards.size
  }

  /** Create a new HoardCollection containing only
  * hoards with known geographic location.
  */
  def located: HoardCollection = {
     HoardCollection(hoards.filter(_.geo !=  None))
  }

  /** Set of mints represented in this collection
  * of hoards.
  */
  def mintSet: Set[String] = {
    hoards.flatMap(_.mints).toSet
  }

  def toKml: String = {
    preface + hoards.map(_.kmlPoint).mkString("\n") + trail
  }

}
