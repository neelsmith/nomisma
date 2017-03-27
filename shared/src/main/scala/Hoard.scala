package edu.holycross.shot.nomisma

//import com.esri.core.geometry._
import scala.xml._

import scala.scalajs.js
import js.annotation.JSExport

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
@JSExport case class Hoard (
  id: String,
  label: String,
  closingDate: Option[ClosingDate],
  mints: Vector[String],
  geo: Option[Point]
) {


  def pointAverage: Option[Integer] = {
    closingDate match {
      case None => None
      case cd: Some[ClosingDate] => Some(cd.get.pointAverage)
    }
  }


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
      println("\t" + prettyId(m))
    }
  }

  def geoString : String = {
    geo match {
      case None => ""
      case ptOpt: Some[Point] => {
        val pt = ptOpt.get
        pt.x.toString +","+pt.y.toString
      }
    }
  }

  def mintsHtml: String = {
    val wrapped = mints.map {
      s => "<li><a href='" + urlFromId(s) + "'>" + prettyId(s) + "</a></li>"
    }
    "<ul>" + wrapped.mkString("\n") + "</ul>"
  }
  def kmlPoint: String = {

    s"""
    <Placemark>
    <name>${id}</name>
    <description><p>${dateLabel}</p>""" +
    s"""${mintsHtml}</description>
    <Point>
      <coordinates>${geoString},0</coordinates>
    </Point>
    </Placemark>
    """
  }
}
