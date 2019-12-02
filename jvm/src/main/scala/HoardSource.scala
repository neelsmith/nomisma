package edu.holycross.shot.nomisma

import scala.io.Source
import java.io.File

import scala.xml._



import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map


/** Factory for generating [[HoardCollection]]s and for
* retrieving spatial data for sets of locations.
*/
object HoardSource extends LogSupport {

  /** Create a [[HoardCollection]] from a file in the RDF format
  * used by `nomisma.org`.
  *
  * @param fName Name of RDF file.
  */
  def fromFile(fName : String) : HoardCollection = {
    fromRdf(Source.fromFile(fName).getLines.mkString("\n"))
  }


  /** Retreive RDF data from a URL.
  *
  * @param url Location of RDF source file.
  */
  def rdfForId(url: String): Option[String] = {
    try {
      Some(scala.io.Source.fromURL(url).mkString)
    } catch {
      case e: Throwable => {
        warn("Couldn't get to url " + url)
        None
      }
    }
  }


  /** Retreive RDF data from a File.
  *
  * @param url Location of RDF source file.
  */
  def rdfForFile(f: File): Option[String] = {
    try {
      Some(scala.io.Source.fromFile(f).mkString)
    } catch {
      case e: Throwable => {
        warn("Couldn't get contents from File " + f)
        None
      }
    }
  }



  /** Create an optional [MintPoint] by consulting nomisma.org's github repo.
  *
  * @param mint String identifier for mint to look up.
  *
  */
  def geoForMint(mint: String): Option[MintPoint] = {
    val urlBase = "https://raw.githubusercontent.com/nomisma/data/master/id/"

    val url = urlBase + mint + ".rdf"
    debug("GET URL " + url)
    rdfForId(url) match {
      case None => None
      case rdf: Some[String] => {
        val root = XML.loadString(rdf.get)
        val spatialThings = root \\ "SpatialThing"
        if (spatialThings.size != 1) {
          warn("No spatial data for " + mint + "(url " + url + ")")
          None
        } else {
          try {
            val spatial = spatialForNode(spatialThings(0))
            Some(MintPoint(spatial._1 , spatial._2))
          } catch {
            case e : Throwable => {
              warn("Something went wrong: " + e)
              None
            }
          }
        }
      }
    }
  }

  /** Map a set of mint IDs to geometric locations
  * by looking up RDF from nomisma.org github contrent.
  *
  * @param mints Set of mint IDs to look up.
  */
  def geoForMints(mints: Set[String]): MintPointCollection = {
    var rslt = scala.collection.mutable.ArrayBuffer[MintPoint]()

    for (m <- mints)  {
      val geo = geoForMint(m)
      geo match {
        case None =>
        case pt : Some[MintPoint] => rslt += pt.get
      }

    }
    MintPointCollection(rslt.toVector)
  }




  /**
  */
  def contentsGraph(hoardCollection: HoardCollection): ContentsGraphCollection = {
    var rslt = scala.collection.mutable.ArrayBuffer[ContentsGraph]()
    val mintsGeo = geoForMints(hoardCollection.located.mintSet)

    debug("Analyzing " + hoardCollection.size + " hoards.")
    var count = 0
    for (h <- hoardCollection.hoards) yield {
      count = count + 1
      debug("Hoard "+ count)
      // get list of mints, and collection their geo.

      try {
        val cg = ContentsGraph(h.id,h.geo.get,mintsGeo.forMints(h.mints).mintPoints)
        rslt += cg
      } catch {
        case e: Throwable => warn("Failed on " + h.id)
      }
    }
    ContentsGraphCollection(rslt.toVector)
  }

    def fromRdf(rdf: String) : HoardCollection = {
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
            hoardId = UrlManager.idFromUrl(att.value.text)
          } else {}
        }
        val label = ch \ "prefLabel"

        val mintList = ch \\ "hasMint"
        var mints = ArrayBuffer.empty[String]
        for (m <- mintList) {
          val attV = m.attributes.toVector
          for (a <- attV) {
            if (a.key == "resource") {
              mints += UrlManager.idFromUrl(a.value.text)
            } else {}
          }
        }
        val closingNodes = ch \\ "hasYearRange"
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



      /** Create [[YearRange]] from informaiton in the
      * parsed RDF XML for a hoard.
      *
      * @param hoardNode Parsed `Hoard` element in RDF XML used
      * by `nomisma.org`.
      */
      def closingDate(hoardNode: scala.xml.Node) : Option[YearRange] = {
        val rangeVals = hoardNode \\ "hasStartDate"
        rangeVals.size match {
          case 0 =>
            try {
              Some(YearRange(hoardNode.text.toInt, None))
            } catch {
              case e: java.lang.NumberFormatException => {
                warn("UNABLE TO PARSE "+ hoardNode.text + s" (length ${hoardNode.text.size})")
                None
              }
            }
          case 2 =>
          try {
           Some(YearRange(rangeVals(0).text.toInt,rangeVals(1).text.toInt))
          } catch {
            case e: java.lang.NumberFormatException => {
              warn("UNABLE TO PARSE "+ hoardNode.text)
              None
            }
          }

          case _ => None
        }
      }
}
