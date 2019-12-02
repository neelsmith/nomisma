package edu.holycross.shot.nomisma

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.histoutils._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


import scala.scalajs.js.annotation._
/** The contents of an edition of OCRE.
*
* @param issues
* @param mintsGeo
*/
@JSExportTopLevel("Ocre")
case class Ocre(
  issues:  Vector[OcreIssue],
  mintsGeo: MintPointCollection = MintPointCollection(Vector.empty[MintPoint])
) extends LogSupport {

  /** Number of issues in this OCRE.*/
  def size : Int = issues.size

  /** Create an ocho2 Corpus of coin legends. */
  def corpus: Corpus = {
    Corpus(issues.map(_.textNodes).flatten)
  }

  /** Create CEX string representing each issue as one
  * row of data.
  */
  def cex: String = {
    val headerLine = "ID#Label#Denomination#Metal#Authority#Mint#Region#ObvType#ObvLegend#ObvPortraitId#RevType#RevLegend#RevPortraitId#StartDate#EndDate\n"
    headerLine + issues.map(_.cex).mkString("\n")
  }

  /** Compose KML for all mints present in this [[Ocre]].
  */
  def kml : String = {
    mintsGeo.forMints(issues.map(_.mint)).mintPoints.map(_.toKml).mkString("\n\n")
  }

  def take(n: Int): Ocre = {
    Ocre(issues.take(n), mintsGeo)
  }


  /** Create histogram of values for a given property.
  *
  * @param Name of property to create histogram for.
  */
  def histogram(propertyName: String) : Histogram[String] = {
    propertyName match {
      case "denomination" => {
        val validValues = hasDenomination.issues.map(_.denomination)
        val frequencies = validValues.groupBy(denom => denom).toVector.map{ case (k,v) => Frequency(k,v.size)}
        Histogram(frequencies)
      }
      case "material" => {
        val validValues = hasMaterial.issues.map(_.material)
        val frequencies = validValues.groupBy(metal => metal).toVector.map{ case (k,v) => Frequency(k,v.size)}
        Histogram(frequencies)
      }
      case "authority" => {
        val validValues = hasAuthority.issues.map(_.authority)
        val frequencies = validValues.groupBy(auth => auth).toVector.map{ case (k,v) => Frequency(k,v.size)}
        Histogram(frequencies)
      }
      case "mint" => {
        val validValues = hasMint.issues.map(_.mint)
        val frequencies = validValues.groupBy(mint => mint).toVector.map{ case (k,v) => Frequency(k,v.size)}
        Histogram(frequencies)
      }
      case "region" => {
        val validValues = hasRegion.issues.map(_.region)
        val frequencies = validValues.groupBy(reg => reg).toVector.map{ case (k,v) => Frequency(k,v.size)}
        Histogram(frequencies)
      }
      case "obvPortraitId" => {
        val validValues = hasObvPortraitId.issues.map(_.obvPortraitId)
        val frequencies = validValues.groupBy(port => port).toVector.map{ case (k,v) => Frequency(k,v.size)}
        Histogram(frequencies)
      }
      case "revPortraitId" => {
        val validValues = hasObvPortraitId.issues.map(_.revPortraitId)
        val frequencies = validValues.groupBy(port => port).toVector.map{ case (k,v) => Frequency(k,v.size)}
        Histogram(frequencies)
      }

      case _ => {
        warn("Unrecognized name for controlled-vocabulary property: " + propertyName)
        Histogram(Vector.empty[Frequency[String]])
      }
    }
  }

  def denominationList: Vector[String] = issues.map(_.denomination).distinct.sorted
  def hasDenomination: Ocre = {
    val denomIssues = issues.filter(issue => (issue.denomination.trim != "none") && ! issue.denomination.trim.contains("uncertain"))
    Ocre(denomIssues)
  }

  def materialList : Vector[String] = issues.map(_.material).distinct.sorted
  def hasMaterial: Ocre = {
    val materialIssues = issues.filter(issue => (issue.material.trim != "none"))
    Ocre(materialIssues)
  }

  def authorityList : Vector[String] = issues.map(_.authority).distinct.sorted
  def hasAuthority: Ocre = {
    val authIssues = issues.filter(issue => (issue.authority.trim != "none") && ! issue.authority.trim.contains("uncertain"))
    Ocre(authIssues)
  }

  def mintList : Vector[String] = issues.map(_.mint).distinct.sorted
  def hasMint: Ocre = {
    val mintIssues = issues.filter(issue => (issue.mint.trim != "none") && ! issue.mint.trim.contains("uncertain"))
    Ocre(mintIssues)
  }


  def regionList : Vector[String] = issues.map(_.region).distinct.sorted
  def hasRegion: Ocre = {
    val regionIssues = issues.filterNot(_.region.contains("uncertain")).filterNot(_.region.contains("none"))
    Ocre(regionIssues)
  }

  def obvPortraitIdList: Vector[String] = {
    issues.filter(_.obvPortraitId.nonEmpty).map(_.obvPortraitId).distinct.sorted
  }
  def hasObvPortraitId: Ocre = {
    val oPortIssues = issues.filter(_.obvPortraitId.nonEmpty)
    Ocre(oPortIssues)
  }

  def hasObvLegend: Ocre = {
    val oLegendIssues = issues.filter(_.obvLegend.nonEmpty)
    Ocre(oLegendIssues)
  }

  def hasObvType: Ocre = {
    val oTypeIssues = issues.filter(_.obvType.nonEmpty)
    Ocre(oTypeIssues)
  }

  def hasRevLegend: Ocre = {
    val rLegendIssues = issues.filter(_.revLegend.nonEmpty)
    Ocre(rLegendIssues)
  }

  def hasRevType: Ocre = {
    val rTypeIssues = issues.filter(_.revType.nonEmpty)
    Ocre(rTypeIssues)
  }

  def revPortraitIdList: Vector[String] = {
    issues.filter(_.revPortraitId.nonEmpty).map(_.revPortraitId).distinct.sorted
  }
  def hasRevPortraitId: Ocre = {
    val rPortIssues = issues.filter(_.revPortraitId.nonEmpty)
    Ocre(rPortIssues)
  }

  def datable: Ocre = {
    val datedIssues = issues.filter(_.dateRange != None)
    Ocre(datedIssues)
  }


  // Naively assuming that there will be at least one datable
  // issue...  Should this be an Opt?
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


  def authoritiesForYear(yr: Int) :  Vector[String] = {
    val auths = byAuthority
    debug("Check on " + yr)
    val matches = for (auth <- auths.keySet.toVector) yield {
      print("Authority " + auth + "....")
      val contained = auths(auth).dateRange.contains(yr)
      debug(contained)
      if (contained) {
        Some(auth)
      } else {
        None
      }
    }
    matches.flatten
  }
  def authorityLabelForYear(yr: Int): String = {
    authoritiesForYear(yr).mkString(", ")
  }
}



/*
  def rangesByAuthority: Map[String, YearRange] = {
  }*/



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
