package edu.holycross.shot.nomisma
import edu.holycross.shot.cite._


import scala.scalajs.js.annotation._


/** A class representing a single issue in OCRE.
*/
@JSExportTopLevel("OcreIssue")
case class OcreIssue(
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
  ) extends NomismaEntity {


  /** Construct URL used by nomisma.org as an identifier.
  */
  def urlString = {
    "http:nomisma.org/id/" + id
  }

  /** Construct a Cite2Urn for this issue.
  */
  def urn: Cite2Urn = {
    Cite2Urn("urn:cite2:nomisma:ocre.hc:" + id)
  }

  /** Construct human-readable label for this issue.*/
  def label = labelText



  def cex: String  = {
    //ID#Label#Denomination#Metal#Authority#Mint#Region#ObvType#ObvLegend#ObvPortraitId#RevType#RevLegend#RevPortraitId#StartDate#EndDate
    val basic =
    s"${id}#${labelText}#${denomination}#${material}#${authority}#${mint}#${region}#${obvType}#${obvLegend}#${obvPortraitId}#${revType}#${revLegend}#${revPortraitId}#"
    val dateCex = dateRange match {
      case None => "##"
      case _ => dateRange.get.cex()
    }
    basic + dateCex
  }

  def kml: String = ""
/*
  def kml: String = {
    mintGeo match {
      case None => ""
      case _ => mintGeo.get.toKml
    }
  }*/
}



object OcreIssue {

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

  /** Construct an [[OcreIssue]] from a line of CEX data.
  *
  * @param cex One of CEX data for an [[OcreIssue]].
  */
  def apply(cex: String) : OcreIssue = {
    def cols = cex.split("#")
    if (cols.size < 15) {
      throw new Exception("Could not parse CEX string for OcreIssue: too few columns in " + cex)
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
        println(s"For coin ${cols(0)}, bad year range: " + t.toString)
        None
      }
    }

    OcreIssue(cols(0), cols(1), cols(2), cols(3), cols(4), cols(5), cols(6).trim, cols(7).trim, cols(8).trim, cols(9).trim, cols(10).trim, cols(11).trim, cols(12).trim, yearRange)
  }
}
