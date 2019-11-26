package edu.holycross.shot.nomisma
import scala.scalajs.js

import scala.scalajs.js.annotation._

/** Closing date of a hoard as modelled by `nomisma.org`.
* Closing date may be either a single integer year, or a
* pair of integers for a range of years.
*
* @param year1 Earlier date in range, or single point.
* @param year2 Later date in range, if any.
*/
@JSExportTopLevel("YearRange")
case class YearRange (year1: Int, year2: Option[Int] = None) {
  require (year1 != 0, "There is no year 0 in our era.")


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
    year2 match {
      case d: Some[Int] => s"${year1}:${d.get}"
      case _ => year1.toString
    }
  }

  def toString(separator: String): String = {
    year2 match {
      case d: Some[Int] => s"${year1}${separator}${d.get}"
      case _ => year1.toString
    }
  }

  def csv(separator: String): String = {
    year2 match {
      case d: Some[Int] => s"${year1}${separator}${d.get}"
      case _ => year1.toString + separator
    }
  }


}

/** Factory for creating [[YearRange]] objects from two
* integer values.
*/
object YearRange {

  /** Create [[YearRange]] object from two
  * integer values.
  *
  * @param year1 Earlier date in range.
  * @param year2 Later date in range.
  */
  def apply(year1: Int, year2: Int): YearRange = {
    YearRange(year1, Some(year2))
  }
}
