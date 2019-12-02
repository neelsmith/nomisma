package edu.holycross.shot.nomisma

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


import scala.scalajs.js.annotation._
/** */
@JSExportTopLevel("UrlManager")
case class UrlManager(
  pairs: Vector[UrlUrnPair]
) extends LogSupport {
  
  def size : Int = pairs.size
}

object UrlManager {

  /** Construct a [[UrlManager]] from a single string
  * with pairing data in delimited text format.
  * @param cex Delimited-text pairings of URLs and URNs,
  * one per line.
  */
  def apply(data: String, delimiter: String = "\t") : UrlManager = {
    val lns = data.split("\n").toVector
    val pairings: Vector[UrlUrnPair] = lns.map(UrlUrnPair(_, delimiter))
    UrlManager(pairings)
  }

  def ricIdFromUrl(lod: String): String = {
    lod.
    replaceFirst("http://numismatics.org/ocre/id/ric.", "").
    replaceFirst("1\\(2\\)", "1_2").
    replaceFirst("zeno\\(1\\)_e","zeno_1_e").
    replaceFirst("zeno\\(2\\)_e","zeno_2_e").
    replaceFirst("2_1\\(2\\)","2_1_2").
    replaceFirst("5.gall\\(1\\)", "5.gall_1").
    replaceFirst("5.gall\\(2\\)", "5.gall_2").
    replaceFirst("5.gall_sala\\(1\\)","5.gall_sala_1").
    replaceFirst("5.gall_sala\\(2\\)","5.gall_sala_2").
    replaceFirst("5.sala\\(1\\)", "5.sala_1").
    replaceFirst("5.sala\\(2\\)", "5.sala_2")
  }

  /** Extract the unique ID value from a long
  * identifying URL.
  *
  * @param lod Linked Open Data URL.
  */
  def idFromUrl (lod: String) : String = {
    lod.replaceAll(".+/","").replaceAll("#.+","")
  }

  /** Pretty print ID value.
  *
  * @param id ID value.
  */
  def prettyId(id: String) : String = {
    id.replaceAll("_"," ").split(' ').map(_.capitalize).mkString(" ")
  }

  /** Create `nomisma.org` URL from an ID value.
  *
  * @param id ID value.
  */
  def urlFromId (id: String) : String = {
    "http://nomisma.org/id/" + id
  }
}
