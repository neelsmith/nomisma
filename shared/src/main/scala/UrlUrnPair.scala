package edu.holycross.shot.nomisma

import edu.holycross.shot.cite._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

import scala.scalajs.js.annotation._


/** Association of a nomisma.org URL identifier with
* a syntactically valid Cite2Urn.
*
* @param urlString URL value used by nomisma.org as an identifier.
* @param urn Cite2Urn corresponding to urlString.
*/
@JSExportTopLevel("UrlUrnPair")
case class UrlUrnPair(
  urlString: String,
  urn: Cite2Urn
)  {

}

object UrlUrnPair extends LogSupport {

  /** Construct a UrlUrnPair from a single line of delimited text.
  *
  * @param cex Delimited text string pairing URL and URN values.
  */
  def apply(cex: String, delimiter: String = "#"): UrlUrnPair = {
    val cols = cex.split(delimiter)
    try {
      UrlUrnPair(cols(0), Cite2Urn(cols(1)))
    } catch {
      case t: Throwable => {
        warn("Unable to form UrlUrnPair from string " + cex)
        throw t
      }
    }
  }
}
