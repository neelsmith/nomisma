package edu.holycross.shot.nomisma

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.histoutils._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


/** The contents of a nomisma.org catalogs of issues,
* works with both RIC (Ocre) and RRC (Rrco).
*
* @param issues
* @param mintsGeo
*/
trait IssueCollection extends LogSupport {

  def issues:  Vector[NomismaIssue]
  def mintsGeo: MintPointCollection

  //def ++(issueCollection: IssueCollection) : IssueCollection

  /** Number of issues in this [[IssueCollection]].*/
  def size : Int = issues.size

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


  /** Retrieve [[NomismaIssue]] for a given ID, if it exists.
  *
  * @param idVal ID value to look for.
  */
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



  /** Controlled vocabulary list for denominations.*/
  def denominationList: Vector[String] = issues.map(_.denomination).distinct.sorted
  /** Create a new [[Ocre]] including only issues that have a denomination value.*/
  def hasDenomination: IssueCollection

  def materialList : Vector[String] = issues.map(_.material).distinct.sorted
  /** Create a new [[Ocre]] including only issues that have a value for material.*/
  def hasMaterial: IssueCollection

  def authorityList : Vector[String] = issues.map(_.authority).distinct.sorted
  /** Create a new [[Ocre]] including only issues that have a value for denomination.*/
  def hasAuthority: IssueCollection

  def mintList : Vector[String] = issues.map(_.mint).distinct.sorted
  /** Create a new [[Ocre]] including only issues that have a value for mint.*/
  def hasMint: IssueCollection /* = {
    val mintIssues = issues.filter(issue => (issue.mint.trim != "none") && ! issue.mint.trim.contains("uncertain"))
    Ocre(mintIssues)
  }*/


  def regionList : Vector[String] = issues.map(_.region).distinct.sorted
  /** Create a new [[Ocre]] including only issues that have a value for region.*/
  def hasRegion: IssueCollection

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

  def dateSpan: Int = {
    if (minDate == maxDate) {
      1
    } else {
      (maxDate - minDate) + 1
    }
  }

  def dateRange: YearRange = {
    if (minDate == maxDate) {
      YearRange(minDate, None)
    } else {
      YearRange(minDate, Some(maxDate))
    }
  }

  /** Group issues by issuing authority and sort chronologically.*/
  def byAuthority : Vector[(String, IssueCollection)]

  def issuesForAuthority(auth: String) : Vector[NomismaIssue]

  def issueFrequency(auth: String): Float = {
    val authIssues = issuesForAuthority(auth)
    authIssues.size * 1.0f / dateSpan
  }


  def authoritiesForYear(yr: Int) :  Vector[String] = {
    val auths = byAuthority
    debug("Check on " + yr)

    val authNames = auths.map(_._1)
    val authOcres = auths.map(_._2)

    val matches = for (  (authOcre, idx)  <- authOcres.zipWithIndex) yield {
      print("Authority " + authNames(idx) + "....")

      val contained = authOcre.dateRange.contains(yr)
      debug(contained)
      if (contained) {
        Some(authNames(idx))
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
