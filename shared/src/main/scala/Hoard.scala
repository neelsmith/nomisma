package edu.holycross.shot.nomisma

import com.esri.core.geometry._


/** A coin hoard.
*
* @param id Unique identifier: last element of `nomisma.org` URL identifier.
* @param label Human-readable label; contents of `prefLabel` element
* in `nomisma.org` RDF XML.
* @param closingDate Closing date of the hoard.
* @param mints Mints represented in this hoard, identified by
* `nomisma.org` identifier.
* @param geo Location of hoard as a geographic point.
*/
case class Hoard (
  id: String,
  label: String,
  closingDate: Option[ClosingDate],
  mints: Vector[String],
  geo: Option[Point]
) {

  /** Formatted string for closing date.
  */
  def dateLabel: String = {
    closingDate match {
      case None => "No date given"
      case _ => "Date: " + closingDate.get
    }
  }

  def prettyPrint = {
    println(s"${label} (${id})")
    println(dateLabel)
    geo match {
      case None => println("Location unknown")
      case _ => println("Location: " + geo.get)
    }
    println("Contains coins from mints:")
    for (m <- mints) {
      println("\t" + m.split(' ').map(_.capitalize).mkString(" "))
    }
  }


// lOD URL for mint!
/*
  def kmlPoint: String = {
    val mintsList = mints.mkString("\n")
    """
    <Placemark>
    <name>${id}</name>
    <description>${dateLabel}\n\n""" +
    raw."""${mintsList}</description>
    <Point>
      <coordinates>${geo.getOrElse("")},0</coordinates>
    </Point>
    </Placemark>
    """
  }
  */
}
