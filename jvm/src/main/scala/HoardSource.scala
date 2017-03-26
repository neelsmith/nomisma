package edu.holycross.shot.nomisma

import scala.io.Source
import scala.xml._

import com.esri.core.geometry._

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map


/** Factory for generating [[HoardCollection]]s and for
* retrieving spatial data for sets of locations.
*/
object HoardSource {

  /** Create a [[HoardCollection]] from a file in RDF format
  * used by `nomisma.org`.
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
        case s: Some[Point] => hoards += Hoard(hoardId,label.text,closing,mints.toVector,s )
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
    (hoardKey,new Point(lon.text.toDouble,lat.text.toDouble))
  }


  /** Convert a sequence of `SpatialThing` nodes to
  * a map of hoard to coordinate pairs.
  *
  * @param nodeSeq Sequence of `SpatialThing` nodes.
  */
  def spatialIndex(nodeSeq : NodeSeq): scala.collection.mutable.Map[String,Point] = {
    var idx = scala.collection.mutable.Map[String,Point]()
    for (n <- nodeSeq) {
      val spatial = spatialForNode(n)
      idx += (spatial._1 -> spatial._2)
    }
    idx
  }



  /** Create [[ClosingDate]] from informaiton in the
  * parsed RDF XML for a hoard.
  *
  * @param hoardNode Parsed `Hoard` element in RDF XML used
  * by `nomisma.org`.
  */
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

  def geoForMint(mint: String): MintPoint = {
    val urlBase = "https://raw.githubusercontent.com/nomisma/data/master/id/"

    val url = urlBase + mint + ".rdf"
    val rdf = scala.io.Source.fromURL(url).mkString

    try {
      val root = XML.loadString(rdf)
      val spatialThings = root \\ "SpatialThing"
      val spatial = spatialForNode(spatialThings(0))
      MintPoint(spatial._1 , spatial._2)
    } catch {
      case e : Throwable => {
        println("Something went wrong: " + e)
        MintPoint(mint, new Point())
      }
    }
  }

  /** Map a set of mint IDs to geometric locations
  * by looking up RDF from nomisma.org github contrent.
  *
  * @param mints Set of mint IDs to look up.
  */
  def geoForMints(mints: Set[String]): Vector[MintPoint] = {//: Map[String,Point] = {
    var rslt = scala.collection.mutable.ArrayBuffer[MintPoint]()
    for (m <- mints)  {
      val geo = geoForMint(m)
      rslt += geo
    }
    rslt.toVector
  }

  def contentsGraph(hoardCollection: HoardCollection) = {
    for (h <- hoardCollection.hoards) yield {
      // get list of mints, and collection their geo.
      val mintsGeo = geoForMints(h.mints.toSet)
      ContentsGraph(h.id,h.geo.get,mintsGeo)
    }
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
