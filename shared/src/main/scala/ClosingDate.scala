package edu.holycross.shot.nomisma
import scala.scalajs.js
import js.annotation.JSExport

/** Closing date of a hoard as modelled by `nomisma.org`.
* Closing date may be either a single integer year, or a
* pair of integers for a range of years.
*
* @param d1 Earlier date in range, or single point.
* @param d2 Later date in range, if any.
*/
@JSExport case class ClosingDate (d1: Integer, d2: Option[Integer] = None) {
  require (d1 != 0, "There is no year 0 in our era.")


  /** True if values for dates are valid.
  */
  def rangeOK = {
    d2 match {
      case None =>  true
      case _ => (d2.get > d1)
    }
  }
  require(rangeOK,s"Date range must be expressed in order earlier to later, not ${d1} to ${d2.get}")


  /** Representation of date as a single integer value.
  */
  def pointAverage: Integer = {
    d2 match {
      case None => d1
      case i: Some[Integer] => (d1 + i.get) / 2
    }
  }


  /** Override default string display.
  */
  override def toString = {
    d2 match {
      case d: Some[Integer] => s"${d1}:${d.get}"
      case _ => d1.toString
    }
  }


}

/** Factory for creating [[ClosingDate]] objects from two
* integer values.
*/
object ClosingDate {

  /** Create [[ClosingDate]] object from two
  * integer values.
  *
  * @param d1 Earlier date in range.
  * @param d2 Later date in range.
  */
  def apply(d1: Integer, d2: Integer): ClosingDate = {
    ClosingDate(d1, Some(d2))
  }
}
