package edu.holycross.shot.nomisma

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.histoutils._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


/** The contents of a nomisma.org catalog of issues:
* RIC (Ocre), RRC (Rrco).
*
* @param issues
* @param mintsGeo
*/
trait IssueCollection extends LogSupport {
  def issues:  Vector[NomismaIssue]
  def mintsGeo: MintPointCollection


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

  /** Compose KML for all mints present in this [[IssueCollection]].
  */
  def kml : String = {
    mintsGeo.forMints(issues.map(_.mint)).mintPoints.map(_.toKml).mkString("\n\n")
  }


  def issue(idVal: String) : Option[NomismaIssue] = {
    val matches = issues.filter(_.id == idVal)
    matches.size match {
      case 0 => {
        warn("No match for ID " + idVal)
        None
      }
      case 1 => Some(matches(0))
      case _ => {
        warn("Serious error in your data:  somehow matched " + matches.size + " issues for supposedly unique ID " + idVal)
        None
      }
    }
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
  def hasDenomination: IssueCollection /*= {
    val denomIssues = issues.filter(issue => (issue.denomination.trim != "none") && ! issue.denomination.trim.contains("uncertain"))
    Ocre(denomIssues)
  }*/

  def materialList : Vector[String] = issues.map(_.material).distinct.sorted
  def hasMaterial: IssueCollection /*= {
    val materialIssues = issues.filter(issue => (issue.material.trim != "none"))
    Ocre(materialIssues)
  }*/

  def authorityList : Vector[String] = issues.map(_.authority).distinct.sorted
  def hasAuthority: IssueCollection /* {
    val authIssues = issues.filter(issue => (issue.authority.trim != "none") && ! issue.authority.trim.contains("uncertain"))
    Ocre(authIssues)
  }*/

  def mintList : Vector[String] = issues.map(_.mint).distinct.sorted
  def hasMint: IssueCollection /* = {
    val mintIssues = issues.filter(issue => (issue.mint.trim != "none") && ! issue.mint.trim.contains("uncertain"))
    Ocre(mintIssues)
  }*/


  def regionList : Vector[String] = issues.map(_.region).distinct.sorted
  def hasRegion: IssueCollection  /*= {
    val regionIssues = issues.filterNot(_.region.contains("uncertain")).filterNot(_.region.contains("none"))
    Ocre(regionIssues)
  }*/

  def obvPortraitIdList: Vector[String]  = {
    issues.filter(_.obvPortraitId.nonEmpty).map(_.obvPortraitId).distinct.sorted
  }
  def hasObvPortraitId: IssueCollection /*= {
    val oPortIssues = issues.filter(_.obvPortraitId.nonEmpty)
    Ocre(oPortIssues)
  }*/

  def hasObvLegend: IssueCollection /*= {
    val oLegendIssues = issues.filter(_.obvLegend.nonEmpty)
    Ocre(oLegendIssues)
  }*/

  def hasObvType: IssueCollection /* = {
    val oTypeIssues = issues.filter(_.obvType.nonEmpty)
    Ocre(oTypeIssues)
  }*/

  def hasRevLegend: IssueCollection /*= {
    val rLegendIssues = issues.filter(_.revLegend.nonEmpty)
    Ocre(rLegendIssues)
  }*/

  def hasRevType: IssueCollection /*= {
    val rTypeIssues = issues.filter(_.revType.nonEmpty)
    Ocre(rTypeIssues)
  }*/

  def revPortraitIdList: Vector[String] = {
    issues.filter(_.revPortraitId.nonEmpty).map(_.revPortraitId).distinct.sorted
  }
  def hasRevPortraitId: IssueCollection /* = {
    val rPortIssues = issues.filter(_.revPortraitId.nonEmpty)
    Ocre(rPortIssues)
  }*/

  def datable: IssueCollection /* = {
    val datedIssues = issues.filter(_.dateRange != None)
    Ocre(datedIssues)
  }*/


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

  def byAuthority : Map[String, IssueCollection] /*= {
    val auths = issues.map(_.authority)
    val authMap = for (auth <- auths) yield {
      val subset = issues.filter(_.authority == auth)
      (auth -> Ocre(subset))
    }
    authMap.toMap
  }*/


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
