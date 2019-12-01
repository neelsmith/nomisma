package edu.holycross.shot.nomisma

import scala.scalajs.js.annotation._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


/**  Legend for a single side of a coin.
*
* @param coin
* @param yearRange
*/
@JSExportTopLevel("IssueYearRange")
case class IssueYearRange (coin: String, yearRange: YearRange)

object IssueYearRange extends LogSupport {

  def yearRangeFromTypeSeries(typeSeries: scala.xml.Node) : Option[YearRange] = {
    val sdates = (typeSeries \\ "hasStartDate")
    debug("Parsing "+ sdates.size + " starting date elements")
    val yearRangeOpt = if (sdates.isEmpty){
      // No date range if type series has no starting date!
      None
    } else if (sdates(0).text.isEmpty) {
      None
    } else {

      val d1 = sdates(0).text.toInt
      //debug("Start date " + d1)
      val edates = (typeSeries \\ "hasEndDate")
      val rng = if (edates.isEmpty) {
        //debug("no end date")
        val yrg = YearRange(d1, None)
        //debug("range " + yrg)
        Some(yrg)

      } else {
        if (edates(0).text.isEmpty) {
            Some(YearRange(d1,None))
        } else {
            val d2 = edates(0).text.toInt
            //debug("End date " + d2)
            val yrg = YearRange(d1,Some(d2))
            //debug("range " + yrg)
            Some(yrg)
        }
      }
      rng
    }
    debug("Computed year range option with " + yearRangeOpt.size + " options.")
    yearRangeOpt
  }

  def datesVector(typeSeriesV: Vector[scala.xml.Node]): Vector[IssueYearRange] = {
    info("Parsing " + typeSeriesV.size + " type series elements.")
    val dateOptions = for (t <- typeSeriesV) yield {
      val coinId = ricIdFromUrl(t.attributes.value.toString)
        // t.attributes.value.toString.replaceFirst("http://numismatics.org/ocre/id/ric.","").replaceFirst("1(2)","1_2" )
      val yearRangeOpts = try {
        val yrOpt = yearRangeFromTypeSeries(t)
        //debug("Computed range " + yrOpt)
        //debug(s"${coinId}, ${yrOpt.get}\n")
        if (yrOpt == None) {
          warn("No starting date found for " + coinId)
          None
        } else {
        //  if (yrOpt.get.isEmpty) {
          //  debug("Empty string found for starting for " + coinId)
          //  None
          //} else {

            Some(IssueYearRange(coinId, yrOpt.get))
          //}

        }

      } catch {
        case t: Throwable => {
          warn("For " + coinId + ", failed to create date range: " + t.toString)
          //debug(s"${coinId}, None\n")
          None
        }
      }
      yearRangeOpts
    }
    val results = dateOptions.toVector.flatten
    info("Extracted "+ results.size + " IssueYearRange objects.")
    results
  }

}
