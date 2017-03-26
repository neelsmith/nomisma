package edu.holycross.shot.nomisma

import scala.io.Source
import scala.xml._

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map

// create a factory method to generate a hoard from an XML source
// 1. create Hoard instances from `Hoard` elements in RDF-XML
// 2. add spatial data from `SpatialThing` elements.
object HoardSource {

  def fromFile(fName : String) : HoardCollection = {
    var hoards = ArrayBuffer.empty[Hoard]
    val root = XML.loadFile(fName)
    val spatialNodes = root \\ "SpatialThing"
    val spatialIdx = spatialIndex(spatialNodes)

    val hoardNodes =  root \\ "Hoard"

    for (ch <- hoardNodes)  {
      var hoardId = ""
      val chAtts = ch.attributes.toVector
      for (att <- chAtts) {
        if (att.key == "about") {
          hoardId = idFromUrl(att.value.text)
        } else {}
      }
      val label = ch \ "prefLabel"

      val mintList = ch \\ "hasMint"
      var mints = ArrayBuffer.empty[String]
      for (m <- mintList) {
        val attV = m.attributes.toVector
        for (a <- attV) {
          if (a.key == "resource") {
            mints += idFromUrl(a.value.text)
          } else {}
        }
      }
      val closingNodes = ch \\ "hasClosingDate"
      val closing =  {
        closingNodes.size match {
          case 0 => None
          case _ => closingDate(closingNodes(0))
        }
      }
      hoards += Hoard(hoardId,label.text,closing,mints.toVector,None )
    }
    HoardCollection(hoards.toVector)
  }



  def spatialIndex(nodeSeq : NodeSeq) = {
    var idx = Map[String,String]()

    for (n <- nodeSeq) {
      var hoardKey = ""
      val attV = n.attributes.toVector
      for (a <- attV) {
        if (a.key == "about") {
          hoardKey = idFromUrl( a.value.text)
        } else {}
      }
      val lat = n \ "lat"
      val lon = n \ "long"
      val geoStr = lon.text + "," + lat.text
      idx += (hoardKey -> geoStr)
    }
    idx
  }


/*
  def computeDate(hoardNode: scala.xml.Node): Option[String] = {
    val rangeVals = hoardNode \\ "hasStartDate"
    rangeVals.size match {
      case 0 => Some(hoardNode.text)
      case 2 => {Some(rangeVals(0).text + ":" + rangeVals(1).text)
      }

      case _ => None
    }
  }*/

  def closingDate(hoardNode: scala.xml.Node) : Option[ClosingDate] = {
    val rangeVals = hoardNode \\ "hasStartDate"
    rangeVals.size match {
      case 0 =>
        try {
          Some(ClosingDate(hoardNode.text.toInt, None))
        } catch {
          case e: java.lang.NumberFormatException => {
            println("UNABLE TO PARSE "+ hoardNode.text + s" (length ${hoardNode.text.size})")
            None
          }
        }
      case 2 =>
      try {
       Some(ClosingDate(rangeVals(0).text.toInt,rangeVals(1).text.toInt))
      } catch {
        case e: java.lang.NumberFormatException => {
          println("UNABLE TO PARSE "+ hoardNode.text)
          None
        }
      }

      case _ => None
    }
  }

}



/*




var hoardId: String = ""
//var geo: Option[String] = None
var hoardDate: Option[String] = None
var mints = ArrayBuffer.empty[String]


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

val kmlDocStr = preface + athensFinds.map(_.kmlPoint).mkString("\n") + trail


import java.io.PrintWriter
new PrintWriter("athenscoins.kml") { write(kmlDocStr); close }

*/
