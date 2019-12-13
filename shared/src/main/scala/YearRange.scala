package edu.holycross.shot.nomisma
import scala.scalajs.js


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


import scala.scalajs.js.annotation._

/** Closing date of a hoard as modelled by `nomisma.org`.
* Closing date may be either a single integer year, or a
* pair of integers for a range of years.
*
* @param year1 Earlier date in range, or single point.
* @param year2 Later date in range, if any.
*/
@JSExportTopLevel("YearRange")
case class YearRange (year1: Int, year2: Option[Int] = None) extends LogSupport {
  require (year1 != 0, {
    warn("Invalid value for YearRange: there is no year 0 in our era." )
    "There is no year 0 in our era."
  })


  /** True if this range contains a given year.
  *
  * @param yr Year to test for.
  */
  def contains(yr: Int): Boolean  = {
    year2 match {
      case None => yr == year1
      case _ => (yr >= year1) && (yr <= year2.get)
    }
  }
  /** True if values for dates are valid.
  */
  def rangeOK = {
    year2 match {
      case None =>  true
      case _ => (year2.get >= year1)
    }
  }
  require(rangeOK,s"Date range must be expressed in order earlier to later, not ${year1} to ${year2.get}")


  /** Representation of date as a single integer value.
  */
  def pointAverage: Int = {
    year2 match {
      case None => year1
      case i: Some[Int] => (year1 + i.get) / 2
    }
  }



  /** Override default string display.
  */
  override def toString = {
    this.toString(":")
  }

  def toString(separator: String): String = {
    year2 match {
      case d: Some[Int] => s"${YearRange.yearString(year1)}${separator}${YearRange.yearString(d.get)}"
      case _ => YearRange.yearString(year1)
    }
  }

  def csv(separator: String): String = {
    year2 match {
      case d: Some[Int] => s"${year1}${separator}${d.get}"
      case _ => year1.toString + separator
    }
  }

  def cex(separator: String = "#"): String = {
    year2 match {
      case d: Some[Int] => s"${year1}${separator}${d.get}"
      case _ => year1.toString + separator
    }
  }


}

/** Factory for creating [[YearRange]] objects from two
* integer values.
*/
object YearRange extends LogSupport {

  /** Create [[YearRange]] object from two
  * integer values.
  *
  * @param year1 Earlier date in range.
  * @param year2 Later date in range.
  */
  def apply(year1: Int, year2: Int): YearRange = {
    YearRange(year1, Some(year2))
  }

  /** Format an Integer value for an individual year
  * as a String BCE or CE.
  *
  * @param yr Integer value for a year.
  */
  def yearString(yr: Int): String = {
    yr match {
      case 0 => {
        warn("Error! There is no year 0!")
        "0 (?)"
      }
      case i if yr > 0 => s"${yr} CE"
      case _ => s"${yr * -1} BCE"
    }
  }
}
