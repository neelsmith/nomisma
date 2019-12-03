package edu.holycross.shot.nomisma
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.scalajs.js.annotation._


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


/** A class representing a single issue in OCRE.
*/
@JSExportTopLevel("GenericIssue")
case class GenericIssue(
    id: String,
    labelText:  String,
    denomination: String,
    material: String,
    authority: String,
    mint: String,
    region: String,
    obvType: String,
    obvLegend: String,
    obvPortraitId: String,
    revType: String,
    revLegend: String,
    revPortraitId: String,
    dateRange: Option[YearRange]
  ) extends NomismaIssue  {



  /** Construct human-readable label for this issue.*/
  def label = labelText


  def kml: String = ""

}


object GenericIssue extends LogSupport {

  /** Parse a string as Int, and return
  * Option[Int]  if successful.
  *
  * @param s String to parse.
  */
  def yearInt(s: String): Option[Int] = {
    try {
      Some(s.toInt)
    } catch {
      case t: Throwable => {
        None
      }
    }
  }

  /** Construct an [[GenericIssue]] from a line of CEX data.
  *
  * @param cex One of CEX data for an [[GenericIssue]].
  */
  def apply(cex: String) : GenericIssue = {

    def cols = cex.split("#")
    if (cols.size < 15) {
      throw new Exception("Could not parse CEX string for GenericIssue: too few columns in " + cex)
    }

    val startDate: Option[Int] = yearInt(cols(13))
    val endDate: Option[Int] = yearInt(cols(14))
    val yearRange = try {
      startDate match {
        case None => None
        case _ => Some(YearRange(startDate.get, endDate))
      }
    } catch {
      case t: Throwable => {
        warn(s"For coin ${cols(0)}, bad year range: " + t.toString)
        None
      }
    }

    GenericIssue(cols(0), cols(1), cols(2), cols(3), cols(4), cols(5), cols(6).trim, cols(7).trim, cols(8).trim, cols(9).trim, cols(10).trim, cols(11).trim, cols(12).trim, yearRange)

  }
}
