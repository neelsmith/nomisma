package edu.holycross.shot.nomisma

import scala.io.Source
import scala.xml._

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map


/** Factory for generating [[HoardCollection]]s and for
* retrieving spatial data for sets of locations.
*/
object HoardSource {

  /** Create a [[HoardCollection]] from a file in
  * nomisma.org's RDF format.
  *
  * @param fName Name of RDF file.
  */
  def fromFile(fName : String) : HoardCollection = {


    val root = XML.loadFile(fName)
    val spatialNodes = root \\ "SpatialThing"
    val hoardNodes =  root \\ "Hoard"

    val spatialIdx = spatialIndex(spatialNodes)

    var hoards =  ArrayBuffer.empty[Hoard]
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

      val geoData = spatialIdx get hoardId
      geoData match {
        case None =>hoards += Hoard(hoardId,label.text,closing,mints.toVector,None )
        case s: Some[String] => hoards += Hoard(hoardId,label.text,closing,mints.toVector,s )
      }

    }
    HoardCollection(hoards.toVector)
  }


  /** Extract coordinate data from a `SpatialThing` node
  * and pair it with the hoard ID.
  *
  * @param n `SpatialThing` node
  */
  def spatialForNode(n: scala.xml.Node) = {
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
    (hoardKey,geoStr)
  }


  /** Convert a sequence of `SpatialThing` nodes to
  * a map of hoard to coordinate pairs.
  *
  * @param nodeSeq Sequence of `SpatialThing` nodes.
  */
  def spatialIndex(nodeSeq : NodeSeq) = {
    var idx = Map[String,String]()
    for (n <- nodeSeq) {
      val spatial = spatialForNode(n)
      idx += (spatial._1 -> spatial._2)
    }
    idx
  }

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


  def geoForMints(mints: Set[String]) = {
    val mintGeo =  Map[String,String]()
    val urlBase = "https://raw.githubusercontent.com/nomisma/data/master/id/"
    for (m <- mints) {
      val url = urlBase + m + ".rdf"
      val rdf = scala.io.Source.fromURL(url).mkString

      try {
        val root = XML.loadString(rdf)
        val spatialThings = root \\ "SpatialThing"
        println(spatialForNode(spatialThings(0)))
        val spatial = spatialForNode(spatialThings(0))
        mintGeo += (spatial._1 -> spatial._2)
      } catch {
        case e : Throwable => println("Something went wrong: " + e)
      }

    }
    mintGeo.toVector
  }
}


/*
val athensFinds = igch.filter(_.mints.contains("athens"))

val plottable = athensFinds.filter(_.geo match {
  case None => false
  case _ => true
})

val kmlDocStr = preface + athensFinds.map(_.kmlPoint).mkString("\n") + trail


import java.io.PrintWriter
new PrintWriter("athenscoins.kml") { write(kmlDocStr); close }

*/
