package edu.holycross.shot.nomisma


/** A collection of [[Hoard]]s.
*
* @param hoards Vector of [[Hoard]]s in this collection.
*/
case class HoardCollection(hoards: Vector[Hoard])  {
  val preface = """<?xml version="1.0" encoding="UTF-8"?>
  <kml xmlns="http://www.opengis.net/kml/2.2">
    <Document>
  """
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

  /** Find unique mints represented in this collection
  * of hoards.
  */
  def mintSet: Set[String] = {
    hoards.flatMap(_.mints).toSet
  }

}
