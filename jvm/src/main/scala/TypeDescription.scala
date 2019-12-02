package edu.holycross.shot.nomisma


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

import scala.scalajs.js.annotation._

/**  Description of a coin's type on a single side.
*
* @param coin Identifier for coin.
* @param side Obverse or Reverse.
* @param description Description of the type.
*/
@JSExportTopLevel("TypeDescription")
case class TypeDescription (coin: String, side: CoinSide, description: String)

object TypeDescription extends LogSupport {



  /** Construct a [[TypeDescription]] from a single line of delimited text data.
  *
  * @param cex Delimited text data.
  */
  def apply(cex: String): Vector[TypeDescription] = {
    val cols = cex.split("#")
    if (cols.size < 3) {
      Vector.empty[TypeDescription]
    } else {
      val otype = TypeDescription(cols(0), Obverse, cols(1))
      val rtype = TypeDescription(cols(0), Reverse, cols(2))
      Vector(otype, rtype)
    }
  }


  /** Given a Vector of OCRE Description nodes,
  * return a Vector of [[TypeDescription]]s.
  *
  * @param descriptionV Vector of OCRE Description nodes.
  */
  def typeDescriptionVector(typeDescrs: Vector[scala.xml.Node]) : Vector[TypeDescription] = {
    info("Parsing " + typeDescrs.size + " RDF Description elements.")
    val descriptionText = for (d <-typeDescrs) yield {
      val rdgs = d \\ "description"
      val rdg = rdgs(0)
      d.attributes.value.toString + "#" + rdg.text
    }
    val descrsRaw = for (d <- descriptionText) yield {
      val triple = d.split("#")
      if (triple.size == 3) {
      val side = coinSide (triple(1))
      val id = UrlManager.ricIdFromUrl(triple(0))

      side match {
        case None => {warn("Bad formatting for coin side: " + triple(1)); None}
        case _  =>   Some(TypeDescription(id,  side.get, triple(2))  )
      }


      } else {
        warn("Failed to parse type description from " + d)
        None
      }
    }
    val results = descrsRaw.filter(_.isDefined).map(_.get)
    info("Extracted " + results.size + " TypeDesription objects.")
    results
  }
}
