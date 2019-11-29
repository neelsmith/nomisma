package edu.holycross.shot.nomisma


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

import scala.scalajs.js.annotation._
/** The contents of an edition of OCRE.
*
* @param issues
* @param legends
* @param typeDescriptions
* @param portraits
*/
@JSExportTopLevel("OcreRdf")
case class OcreRdf(
  issues:  Vector[BasicIssue],
  legends: Vector[Legend],
  typeDescriptions: Vector[TypeDescription],
  portraits: Vector[Portrait],
  dateRanges: Vector[IssueYearRange],
  mintsGeo: MintPointCollection
) extends LogSupport {

  Logger.setDefaultLogLevel(LogLevel.INFO)

  def toOcre(msgPoint: Int = 100): Ocre = {
    Ocre(ocreIssues(msgPoint))
  }

  def sample(i : Int): Ocre = {
    val issueSample = issues.take(i)
    val ocrefied = ocreIssues(issueSample.map(_.id))
    Ocre(ocrefied)
  }
  def ocreIssues(msgPoint: Int) : Vector[OcreIssue] = {
    val ids = issues.map(_.id)
    ocreIssues(ids, msgPoint)
  }
  def ocreIssues(ids: Vector[String], msgPoint: Int = 100): Vector[OcreIssue] = {
    val total = ids.size
    val opts = for ((id,count) <- ids.zipWithIndex) yield {
      if (count % msgPoint == 0) {
        info(s"Converting ${id}: ${count + 1}/${total}")
      }
      ocreIssue(id)
    }
    //val opts = ids.map(ocreIssue(_))
    opts.flatten
  }


  def ocreIssue(id: String) : Option[OcreIssue] = {
    val issueMatches = issues.filter(_.id == id)
     issueMatches.size  match {
      case 1 => {
        val basics = issueMatches(0)
        val geo = mintsGeo.forMint(basics.mint)

        val rTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Reverse)
        val rType = if (rTypeMatches.size == 1) {
          rTypeMatches(0).description
        } else {""}

        val oTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Obverse)
        val oType = if (oTypeMatches.size == 1) {
          oTypeMatches(0).description
        } else {""}

        val rLegendMatches = legends.filter(_.coin == id).filter(_.side == Reverse)
        val rLegend = if (rLegendMatches.size == 1) {
          rLegendMatches(0).legend
        } else {""}

        val oLegendMatches = legends.filter(_.coin == id).filter(_.side == Obverse)
        val oLegend = if (oLegendMatches.size == 1) {
          oLegendMatches(0).legend
        } else {""}

        val oPortaitMatches = portraits.filter(_.coin == id).filter(_.side == Obverse)
        val oPortrait = if (oPortaitMatches.size == 1) {
          oPortaitMatches(0).portrait
        } else {""}

        val rPortaitMatches = portraits.filter(_.coin == id).filter(_.side == Reverse)
        val rPortrait = if (rPortaitMatches.size == 1) {
          rPortaitMatches(0).portrait
        } else {""}


        val dateMatches = dateRanges.filter(_.coin == id)
        val dateRangeOpt = if (dateMatches.size == 1) {
          Some(dateMatches(0).yearRange)
        } else {None}


        Some(OcreIssue(basics.id, basics.labelText, basics.denomination, basics.material, basics.authority, basics.mint, basics.region, oType, oLegend, oPortrait, rType, rLegend,  rPortrait, dateRangeOpt))
      }
      case _ => None
    }
  }
  def ocreIssuesSimplified(ids: Vector[String]): Vector[OcreIssueSimplified] = {
    val opts = ids.map(ocreIssueSimplified(_))
    opts.flatten
  }

  /** Given an object identifier, construct an [[OcreIssueSimplified]].
  *
  * @param id Object identifier for issue.
  */
  def ocreIssueSimplified(id: String) : Option[OcreIssueSimplified] = {
    val issueMatches = issues.filter(_.id == id)
     issueMatches.size  match {
      case 1 => {
        val basics = issueMatches(0)
        val geo = mintsGeo.forMint(basics.mint)

        val rTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Reverse)
        val rType = if (rTypeMatches.size == 1) { Some(rTypeMatches(0))} else {None}

        val oTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Obverse)
        val oType = if (oTypeMatches.size == 1) { Some(oTypeMatches(0))} else {None}

        val rLegendMatches = legends.filter(_.coin == id).filter(_.side == Reverse)
        val rLegend = if (rLegendMatches.size == 1) { Some(rLegendMatches(0))} else {None}

        val oLegendMatches = legends.filter(_.coin == id).filter(_.side == Obverse)
        val oLegend = if (oLegendMatches.size == 1) { Some(oLegendMatches(0))} else {None}

        val oPortrait = None // not yet implemented
        val rPortrait = None // not yet implemented

        Some(OcreIssueSimplified(basics, oType, rType, oLegend, rLegend, oPortrait, rPortrait, geo))
      }
      case _ => None
    }
  }


  def kml : String = {
    mintsGeo.forMints(issues.map(_.mint)).mintPoints.map(_.toKml).mkString("\n\n")
  }
}


object OcreRdf {


  def addGeo(ocre: OcreRdf, geo: MintPointCollection): OcreRdf = {
    OcreRdf(ocre.issues, ocre.legends, ocre.typeDescriptions, ocre.portraits, ocre.dateRanges, geo)
  }
  /**
  *
  * @param ocre Root of parsed OCRE data set.
  */
  def parseRdf(ocre: scala.xml.Elem): OcreRdf = {

    // Enforce uniqe entries.
    val issues = BasicIssue.parseOcreXml(ocre)

    val descrs = ocre \\ "Description"
    val dv = descrs.toVector
    val legends = Legend.legendVector(dv)

    val typeDescrNodes = dv.filter( d => (d  \\ "description").size > 0 )
    val typeDescriptions =  TypeDescription.typeDescriptionVector(typeDescrNodes)

    val portraitElems = dv.filter( d => (d  \\ "hasPortrait").size > 0 )
    val portraits = Portrait.portraitVector(portraitElems)


    val typeData = ocre \\ "TypeSeriesItem"
    val dateRanges = IssueYearRange.datesVector(typeData.toVector)

    OcreRdf(issues, legends, typeDescriptions, portraits, dateRanges, MintPointCollection(Vector.empty[MintPoint]))

  }

}
