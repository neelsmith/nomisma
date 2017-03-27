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
        //MintPoint(mint, new Point())
        throw e
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
