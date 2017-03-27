package edu.holycross.shot.nomisma


import scala.xml._
//import com.esri.core.geometry._
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map
import scala.scalajs.js
import js.annotation.JSExport

/** A collection of [[Hoard]]s.
*
* @param hoards Vector of [[Hoard]]s in this collection.
*/
@JSExport case class HoardCollection(hoards: Vector[Hoard])  {
  /** Preface to KML document.
  */
  val preface = """<?xml version="1.0" encoding="UTF-8"?>
  <kml xmlns="http://www.opengis.net/kml/2.2">
    <Document>
  """

  /** Conclusion to KML document.
  */
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

  /** Set of mints represented in this collection
  * of hoards.
  */
  def mintSet: Set[String] = {
    hoards.flatMap(_.mints).toSet
  }

  def toKml: String = {
    preface + hoards.map(_.kmlPoint).mkString("\n") + trail
  }

}


// factory for making HC from RDF xml string
object HoardCollection {

  def apply(rdf: String) : HoardCollection = {
    val root = XML.loadString(rdf)
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
}
