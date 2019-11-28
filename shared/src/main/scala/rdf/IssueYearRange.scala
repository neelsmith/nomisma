package edu.holycross.shot.nomisma

import scala.scalajs.js.annotation._

/**  Legend for a single side of a coin.
*
* @param coin
* @param yearRange
*/
@JSExportTopLevel("IssueYearRange")
case class IssueYearRange (coin: String, yearRange: YearRange)
