package edu.holycross.shot.nomisma


import scala.scalajs.js
import js.annotation.JSExport


/** A geographic point.
*
* @param x X or longitude value.
* @param y Y or latitude value.
*/
@JSExport case class Point(x: Double, y: Double) {

  /** Use comma-delimited for default toString representation.
  */
  override def toString = {
    x.toString + "," + y.toString
  }


  /** Delimited-text representation of point.
  *
  * @param delimiter String value separating x and y.
  */
  def delimited(delimiter: String) = {
    x.toString + delimiter + y.toString
  }
}
