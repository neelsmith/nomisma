package edu.holycross.shot.nomisma

import scala.io.Source
import scala.xml._

import scala.collection.mutable.ArrayBuffer


// create a factory method to generate a hoard from an XML source
// 1. create Hoard instances from `Hoard` elements in RDF-XML
// 2. add spatial data from `SpatialThing` elements.
object HoardSource {

  def fromFile(fName : String) : HoardCollection = {
    var hoards = ArrayBuffer.empty[Hoard]
    val root = XML.loadFile(fName)
    val spatialNodes = root \\ "SpatialThing"
    val hoardNodes =  root \\ "Hoard"

    def computeDate(hoardNode: scala.xml.Node): Option[String] = {
      val rangeVals = hoardNode \\ "hasStartDate"
      rangeVals.size match {
        case 0 => Some(hoardNode.text)
        case 2 => {Some(rangeVals(0).text + "-" + rangeVals(1).text)
        }

        case _ => None
      }
    }

    for (ch <- hoardNodes)  {
      val hoardId = ch \ "prefLabel"


      val mintList = ch \\ "hasMint"
      var mints = ArrayBuffer.empty[String]
      for (m <- mintList) {
        val attV = m.attributes.toVector
        for (a <- attV) {
          if (a.key == "resource") {
            mints += a.value.text
          } else {}
        }
      }
      hoards += Hoard(hoardId.text,computeDate(ch),mints.toVector,None )
    }
    HoardCollection(hoards.toVector)
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
