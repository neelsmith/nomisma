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
  /*
<<<<<<< HEAD

=======
  val preface = """<?xml version="1.0" encoding="UTF-8"?>
  <kml xmlns="http://www.opengis.net/kml/2.2">
    <Document>
    <Style id="group1">
      <IconStyle>
        <scale>0.5</scale>
        <Icon>
          <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group2">
      <IconStyle>
        <Icon>
          <scale>1.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group3">
      <IconStyle>
        <Icon>
          <scale>8.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group4">
      <IconStyle>
        <Icon>
          <scale>16.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group5">
      <IconStyle>
        <Icon>
          <scale>32.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group6">
      <IconStyle>
        <Icon>
          <scale>64.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
  """
>>>>>>> 3d7500cf10ba0aa4f0147491182cdd723b159a34
*/
  /** Conclusion to KML document.
  */
  val trail = "</Document></kml>"


  /** Number of hoards in the collection.
  */
  def size: Int = {
    hoards.size
  }


  def hasMint : HoardCollection = {
    HoardCollection(hoards.filter(_.mints.nonEmpty))
  }
  /** Create a new HoardCollection containing only
  * hoards with known geographic location.
  */
  def located: HoardCollection = {
    HoardCollection(hoards.filter(_.geo !=  None))
  }

  def dated: HoardCollection = {
    HoardCollection(hoards.filter(_.closingDate !=  None))
  }


  def closingDateVector: Vector[ClosingDate] = {
    dated.hoards.map(_.closingDate.get)
  }

  /** Set of mints represented in this collection
  * of hoards.
  */
  def mintSet: Set[String] = {
    hoards.flatMap(_.mints).toSet
  }

  /** Maximum of all `pointAverage` values in the collection.
  */
  def maxAvgDate = {
    val temp = dated.hoards.map(_.pointAverage.get)
    temp.max
  }

  def maxDate = {
    val d1List = dated.hoards.map(_.closingDate.get.d1)
    //val d2List = dated.hoards.filter(_.closingDate.get.d2 != None).map(_.closingDate.get.d2)
    //if (d1List.max > d2List.max) { d1List.max} else {d2List.max}
    d1List.max

  }

  /** Minimum of all `pointAverage` values in the collection.
  */
  def minAvgDate = {
    val temp = dated.hoards.map(_.pointAverage.get)
    temp.min
  }

  def trimToAvgDateRange(d1: Integer, d2: Integer) = {
    dated.hoards.filter(_.pointAverage.get >= d1).filter(_.pointAverage.get <= d2)
  }

  def csv: String = {
    val hdr = "ID,label,date1,date2,mints,lon,lat\n"
    hdr + hoards.map(_.csv).mkString("\n")
  }

/*
  val maxDate: Integer = {
    val maxD1 = hoards.map(_.closingDate.d1).max
    val maxD2 = hoards.map(_.closingDate.d2).max
    if (maxD1 > maxD2) {
      maxD1
    } else {
      maxD2
    }
  }
*/
/*  def toKml: String = {
    preface + hoards.map(_.kmlPoint).mkString("\n") + trail
  }

<<<<<<< HEAD
*/
  /** Preface to KML document.
  */
  val preface = """<?xml version="1.0" encoding="UTF-8"?>
  <kml xmlns="http://www.opengis.net/kml/2.2">
    <Document>
    <Style id="group1">
      <IconStyle>
        <scale>0.5</scale>
        <Icon>
          <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group2">
      <IconStyle>
        <Icon>
          <scale>1.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group3">
      <IconStyle>
        <Icon>
          <scale>2.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group4">
      <IconStyle>
        <Icon>
          <scale>4.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group5">
      <IconStyle>
        <Icon>
          <scale>6.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group6">
      <IconStyle>
        <Icon>
          <scale>8.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
  """
  /*
=======
  def delimitedText(separator: String = "#"): String = {
    val csvHeader = "id,label,date,lon,lat\n"
    csvHeader + hoards.map(_.delimited(separator)).mkString("\n")
  }
>>>>>>> 3d7500cf10ba0aa4f0147491182cdd723b159a34
*/
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
