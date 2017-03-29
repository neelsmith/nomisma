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



  def sizeUrl: String = {
    mints.size match {
      case n if (n >= 36) => "#group6"
      case n if (n >= 22 && n < 36) => "#group5"
      case n if (n >= 7 && n < 22) => "#group4"
      case n if (n >= 4 && n < 7) => "#group3"
      case n if (n >= 2 && n < 4) => "#group2"
      case n if (n == 1) => "#group1"

    }
  }

  def mintUrl: String = {
    "<a href='http://coinhoards.org/id/" + id + "'>" + label + "</a>"
  }

  def kmlPoint: String = {
    geo match {
      case None => ""
      case _ =>
    raw"""
    <Placemark>
    <name>${id}</name>
    <description><p>${mintUrl}, ${dateLabel}</p>
    ${mintsHtml}</description>
    <styleUrl>${sizeUrl}</styleUrl>
    <Point>
      <coordinates>${geoString},0</coordinates>
    </Point>
    </Placemark>
    """
  }
  }
  def delimited(delimiter: String = "#"): String = {
    val dateDisplay = {
       closingDate match {
         case None => ""
         case _ => closingDate.get
       }
    }

    geo match {
      case None => ""
      case _ =>
        raw"""${id}${delimiter}${label}${delimiter}${dateDisplay}${delimiter}${geo.get.x}${delimiter}${geo.get.y}"""
    }
  }
}
