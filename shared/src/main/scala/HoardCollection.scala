package edu.holycross.shot.nomisma



case class HoardCollection(hoards: Vector[Hoard])  {
  val preface = """<?xml version="1.0" encoding="UTF-8"?>
  <kml xmlns="http://www.opengis.net/kml/2.2">
    <Document>
  """
  val trail = "</Document></kml>"

  def size: Int = {
    hoards.size
  }

  def mintSet: Set[String] = {
    hoards.flatMap(_.mints).toSet
  }

}
