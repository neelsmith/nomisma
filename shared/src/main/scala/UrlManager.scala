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

}

object UrlManager {

  /** Construct a [[UrlManager]] from a single string
  * with pairing data in delimited text format.
  * @param cex Delimited-text pairings of URLs and URNs,
  * one per line.
  */
  def apply(cex: String) : UrlManager = {
    val lns = cex.split("\n").toVector
    val pairings: Vector[UrlUrnPair] = lns.map(UrlUrnPair(_))
    UrlManager(pairings)
  }
}
