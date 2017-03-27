package edu.holycross.shot.nomisma

import scala.io.Source


import scala.xml._

//import com.esri.core.geometry._

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
    HoardCollection(Source.fromFile(fName).getLines.mkString("\n"))
  }

  def rdfForId(url: String): Option[String] = {
    try {
      Some(scala.io.Source.fromURL(url).mkString)
    } catch {
      case e: Throwable => {
        println("Couldn't get to url " + url)
        None
      }
    }
  }

  def geoForMint(mint: String): Option[MintPoint] = {
    val urlBase = "https://raw.githubusercontent.com/nomisma/data/master/id/"

    val url = urlBase + mint + ".rdf"
    println("GET URL " + url)
    rdfForId(url) match {
      case None => None
      case rdf: Some[String] => {
        val root = XML.loadString(rdf.get)
        val spatialThings = root \\ "SpatialThing"
        if (spatialThings.size != 1) {
          println("No spatial data for " + mint + "(url " + url + ")")
          None
        } else {
          try {
            val spatial = spatialForNode(spatialThings(0))
            Some(MintPoint(spatial._1 , spatial._2))
          } catch {
            case e : Throwable => {
              println("Something went wrong: " + e)
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

  def contentsGraph(hoardCollection: HoardCollection) = {
    var rslt = scala.collection.mutable.ArrayBuffer[ContentsGraph]()
    val mintsGeo = geoForMints(hoardCollection.located.mintSet)

    println("Analyzing " + hoardCollection.size + " hoards.")
    var count = 0
    for (h <- hoardCollection.hoards) yield {
      count = count + 1
      println("Hoard "+ count)
      // get list of mints, and collection their geo.

      try {
        val cg = ContentsGraph(h.id,h.geo.get,mintsGeo.forMints(h.mints).mintPoints)
        rslt += cg
      } catch {
        case e: Throwable => println("Failed on " + h.id)
      }
    }
    ContentsGraphCollection(rslt.toVector)
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
