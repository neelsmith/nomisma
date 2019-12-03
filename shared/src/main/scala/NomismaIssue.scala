package edu.holycross.shot.nomisma
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

/** A class representing a single issue in OCRE.
*/
trait NomismaIssue {
    def id: String
    def denomination: String
    def material: String
    def authority: String
    def mint: String
    def region: String
    def obvType: String
    def obvLegend: String
    def obvPortraitId: String
    def revType: String
    def revLegend: String
    def revPortraitId: String
    def dateRange: Option[YearRange]

  /** Construct URL used by nomisma.org as an identifier.
  */
  def urlString : String = {
    "http:nomisma.org/id/" + id
  }

  /** Construct a Cite2Urn for this issue.
  */
  def urn: Cite2Urn

  /** Construct human-readable label for this issue.*/
  def label : String


  /** Data for this issue formatted as a single line in CEX format.*/
  def cex: String  = {
    //ID#Label#Denomination#Metal#Authority#Mint#Region#ObvType#ObvLegend#ObvPortraitId#RevType#RevLegend#RevPortraitId#StartDate#EndDate
    val basic =
    s"${id}#${label}#${denomination}#${material}#${authority}#${mint}#${region}#${obvType}#${obvLegend}#${obvPortraitId}#${revType}#${revLegend}#${revPortraitId}#"
    val dateCex = dateRange match {
      case None => "##"
      case _ => dateRange.get.cex()
    }
    basic + dateCex
  }

  def textNodes: Vector[CitableNode]
  def kml: String
}
