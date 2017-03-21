package edu.holycross.shot.nomisma

//import scala.io.Source
import scala.xml._

import scala.collection.mutable.ArrayBuffer

//val root = XML.loadFile("igch.rdf")

case class Hoard(id: String, dateStr: Option[String], geo: Option[String], mints: Vector[String]) {
  val dateLabel: String = {
    dateStr match {
      case None => "No date given"
      case _ => "Date: " + dateStr.get
    }
  }
  def prettyPrint = {
    println(id)
    println(dateLabel)
    geo match {
      case None => println("Location unknown")
      case _ => println("Location: " + geo.get)
    }
    println("Contains coins from mints:")
    for (m <- mints) {
      println("\t" + m)
    }
  }
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
}

object Hoard {
  // create a factory method to generate a hoard from an XML source
  // 1. create Hoard instances from `Hoard` elements in RDF-XML
  // 2. add spatial data from `SpatialThing` elements.
}

/*
var igch = ArrayBuffer.empty[Hoard]

def computeDate(hoardNode: scala.xml.Node): Option[String] = {
  val rangeVals = hoardNode \\ "hasStartDate"
  rangeVals.size match {
    case 0 => Some(hoardNode.text)
    case 2 => {Some(rangeVals(0).text + "-" + rangeVals(1).text)
    }

    case _ => None
  }
}


var hoardId: String = ""
var geo: Option[String] = None
var hoardDate: Option[String] = None
var mints = ArrayBuffer.empty[String]


for (ch <- root.child)  {

  ch.label match {
    case "Hoard" => {
      //println(hoardId + ":" + geo)
      geo = None
      hoardDate = None
      mints = ArrayBuffer.empty[String]

      val lbl = ch \ "prefLabel"
      hoardId = lbl.text

      val mintList = ch \\ "hasMint"
      for (m <- mintList) {
        val attV = m.attributes.toVector
        for (a <- attV) {
          if (a.key == "resource") {
            mints += a.value.text
          } else {}

        }
      }
      hoardDate = computeDate(ch)



    }
    case "SpatialThing" => {
      val lat = ch \ "lat"
      val lon = ch \ "long"
      geo = Some(lon.text + "," + lat.text)
      igch += Hoard(hoardId, hoardDate, geo, mints.toVector)
    }
    case "#PCDATA" =>
  }
}

val athensFinds = igch.filter(_.mints.contains("http://nomisma.org/id/athens"))

val plottable = athensFinds.filter(_.geo match {
  case None => false
  case _ => true
})

val preface = """<?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://www.opengis.net/kml/2.2">
  <Document>
"""

val trail = "</Document></kml>"


val kmlDocStr = preface + athensFinds.map(_.kmlPoint).mkString("\n") + trail


import java.io.PrintWriter
new PrintWriter("athenscoins.kml") { write(kmlDocStr); close }

*/
