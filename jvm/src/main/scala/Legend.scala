package edu.holycross.shot.nomisma

import scala.scalajs.js.annotation._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

/**  Legend for a single side of a coin.
*
* @param coin Identifier for coin.
* @param side Obverse or Reverse.
* @param legend Text of legend.
*/
@JSExportTopLevel("Legend")
case class Legend (coin: String, side: CoinSide, legend: String)


object Legend extends LogSupport {
  Logger.setDefaultLogLevel(LogLevel.INFO)

  /** Create a [[Legend]] from a single line of delimited text data.
  *
  * @param cex String of CEX data.
  */
  def apply(cex: String): Vector[Legend] = {
    val cols = cex.split("#")
    if (cols.size < 3) {
      Vector.empty[Legend]
    } else {

      val olegend = Legend(cols(0), Obverse, cols(1))
      val rlegend = Legend(cols(0), Reverse, cols(2))
      Vector(olegend, rlegend)
    }
  }


  /** Given a Vector of RDF Description nodes for OCRE data,
  * return a Vector of [[Legend]]s.
  *
  * @param descriptionV Vector of OCRE Description nodes.
  */
  def legendVector(descriptionV: Vector[scala.xml.Node]) : Vector[Legend] = {
    val legendsElems = descriptionV.filter( d => (d  \\ "hasLegend").size > 0 )
    info("Parsing " + legendsElems.size + " RDF Description elements.")
    val legendsText = for (l <-legendsElems) yield {
      val rdgs = l \\ "hasLegend"
      val rdg = rdgs(0)
      l.attributes.value.toString + "#" + rdg.text
    }
    val legendsRaw = for (l <- legendsText) yield {
      val triple = l.split("#")
      if (triple.size == 3) {
        val side = coinSide (triple(1))
        side match {
          case None => None
          case _  => {
            val id = ricIdFromUrl(triple(0))
            Some(Legend(id,  side.get, triple(2) ))
          }
        }

      } else {
        warn("Failed to parse legend structure for " + l)
        None
      }
    }
    val legends = legendsRaw.filter(_.isDefined).map(_.get)
    info("Constructed " + legends.size + " Legends.")
    legends
  }
}
