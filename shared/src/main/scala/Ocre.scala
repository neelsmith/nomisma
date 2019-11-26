package edu.holycross.shot.nomisma


import scala.scalajs.js.annotation._
/** The contents of an edition of OCRE.
*
* @param issues
* @param legends
* @param typeDescriptions
* @param portraits
*/
@JSExportTopLevel("Ocre")
case class Ocre(
  issues:  Vector[OcreIssue],
  mintsGeo: MintPointCollection = MintPointCollection(Vector.empty[MintPoint])
) {

  def size : Int = issues.size

  def kml : String = {
    mintsGeo.forMints(issues.map(_.mint)).mintPoints.map(_.toKml).mkString("\n\n")
  }

  def datable: Ocre = {
    val datedIssues = issues.filter(_.dateRange != None)
    Ocre(datedIssues)
  }

  // Naively assuming that there will be at least one datable
  // issue...
  def minDate: Int = {
    datable.issues.map(_.dateRange.get.year1).min
  }

  def maxDate: Int = {
    val yr2s = datable.issues.map(_.dateRange).flatten.map(_.year2.get)
    yr2s.size match {
      case 0 => datable.issues.map(_.dateRange.get.year1).max
      case _ => yr2s.max
    }
  }

  def dateRange: YearRange = {
    if (minDate == maxDate) {
      YearRange(minDate, None)
    } else {
      YearRange(minDate, Some(maxDate))
    }
  }

  def byAuthority : Map[String, Ocre] = {
    val auths = issues.map(_.authority)
    val authMap = for (auth <- auths) yield {
      val subset = issues.filter(_.authority == auth)
      (auth -> Ocre(subset))
    }
    authMap.toMap
  }
/*
  def rangesByAuthority: Map[String, YearRange] = {
  }*/
}


object Ocre {


  def apply(cex: String, dropHeader: Boolean) : Ocre = {
    val lines = cex.split("\n").toVector
    dropHeader match {
      case true => fromCexStrings(lines.tail)
      case false =>fromCexStrings(lines)
    }
  }

  def fromCexStrings(cexLines : Vector[String]) : Ocre = {
    val issues = cexLines.map(OcreIssue(_))
    Ocre(issues)
  }
  /** Create a new [[Ocre]] instance by associating a [[MintPointCollection]]
  * with the Vector of [[OcreIssue]]s in an existing [[Ocre]].
  *
  * @param ocre Existing [[Ocre]] with [[OcreIssue]]s to use.
  * @param geo [[MintPointCollection]] to use.
  */
  def addGeo(ocre: Ocre, geo: MintPointCollection): Ocre = {
    Ocre(ocre.issues, geo)
  }
}
