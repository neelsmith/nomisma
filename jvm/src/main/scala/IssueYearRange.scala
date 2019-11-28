package edu.holycross.shot.nomisma

import scala.scalajs.js.annotation._

/**  Legend for a single side of a coin.
*
* @param coin
* @param yearRange
*/
@JSExportTopLevel("IssueYearRange")
case class IssueYearRange (coin: String, yearRange: YearRange)

object IssueYearRange {

  def yearRangeFromTypeSeries(typeSeries: scala.xml.Node) : Option[YearRange] = {
    val sdates = (typeSeries \\ "hasStartDate")
    val yearRangeOpt = if (sdates.isEmpty){
      // No date range if type series has no starting date!
      None
    } else if (sdates(0).text.isEmpty) {
      None
    } else {

      val d1 = sdates(0).text.toInt
      //println("Start date " + d1)
      val edates = (typeSeries \\ "hasEndDate")
      val rng = if (edates.isEmpty) {
        //println("no end date")
        val yrg = YearRange(d1, None)
        //println("range " + yrg)
        Some(yrg)

      } else {
        if (edates(0).text.isEmpty) {
            Some(YearRange(d1,None))
        } else {
            val d2 = edates(0).text.toInt
            //println("End date " + d2)
            val yrg = YearRange(d1,Some(d2))
            //println("range " + yrg)
            Some(yrg)
        }
      }
      rng
    }
    //println("Returning computed year range option " + yearRangeOpt)
    yearRangeOpt
  }

  def datesVector(typeSeriesV: Vector[scala.xml.Node]): Vector[IssueYearRange] = {
    val dateOptions = for (t <- typeSeriesV) yield {
      val coinId = t.attributes.value.toString.replaceFirst("http://numismatics.org/ocre/id/ric.","").replaceFirst("1(2)","1_2" )
      val yearRangeOpts = try {
        val yrOpt = yearRangeFromTypeSeries(t)
        //println("Computed range " + yrOpt)
        //println(s"${coinId}, ${yrOpt.get}\n")
        if (yrOpt == None) {
          println("No starting date found for " + coinId)
          None
        } else {
        //  if (yrOpt.get.isEmpty) {
          //  println("Empty string found for starting for " + coinId)
          //  None
          //} else {

            Some(IssueYearRange(coinId, yrOpt.get))
          //}

        }

      } catch {
        case t: Throwable => {
          println("For " + coinId + ", failed to create date range: " + t.toString)
          //println(s"${coinId}, None\n")
          None
        }
      }
      yearRangeOpts
    }
    dateOptions.toVector.flatten
  }

}
