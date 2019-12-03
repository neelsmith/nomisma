package edu.holycross.shot.nomisma
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import scala.scalajs.js.annotation._


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

/** A class representing a single issue in OCRE.
*/
@JSExportTopLevel("CrroIssue")
case class CrroIssue(
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
  ) extends NomismaIssue with NomismaEntity {


  /** Construct a Cite2Urn for this issue.
  */
  def urn: Cite2Urn = {
    Cite2Urn("urn:cite2:nomisma:rrco.hc:" + id)
  }

  /** Construct human-readable label for this issue.*/
  def label = labelText

  /** Construct Vector of CitableNodes for legends in this issue.
  */
  def textNodes: Vector[CitableNode] = {
    val obvUrn = CtsUrn("urn:cts:hcnum:issues.rrco.raw:" + id + ".obv")
    val obvOpt = if (obvLegend.nonEmpty)  {Some(CitableNode(obvUrn, obvLegend))} else {None}
    val revUrn = CtsUrn("urn:cts:hcnum:issues.rrco.raw:" + id + ".rev")
    val revOpt = if (revLegend.nonEmpty)  {Some(CitableNode(revUrn, revLegend))} else {None}
    Vector(obvOpt, revOpt).flatten
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



object CrroIssue extends LogSupport {

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

  /** Construct an [[CrroIssue]] from a line of CEX data.
  *
  * @param cex One of CEX data for an [[CrroIssue]].
  */
  def apply(cex: String) : CrroIssue = {

    def cols = cex.split("#")
    if (cols.size < 15) {
      throw new Exception("Could not parse CEX string for CrroIssue: too few columns in " + cex)
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

    CrroIssue(cols(0), cols(1), cols(2), cols(3), cols(4), cols(5), cols(6).trim, cols(7).trim, cols(8).trim, cols(9).trim, cols(10).trim, cols(11).trim, cols(12).trim, yearRange)

  }
}
