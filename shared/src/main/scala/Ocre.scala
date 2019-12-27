package edu.holycross.shot.nomisma

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.histoutils._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

import scala.scalajs.js.annotation._

/** The contents of an edition of OCRE.
*
* @param issues All issues in this collection.
* @param mintsGeo Geographic data for mints.
*/
@JSExportTopLevel("Ocre")
case class Ocre(
  issues:  Vector[OcreIssue],
  mintsGeo: MintPointCollection = MintPointCollection(Vector.empty[MintPoint])
) extends IssueCollection  with LogSupport {

  Logger.setDefaultLogLevel(LogLevel.INFO)

  /** Create a new [[Ocre]] by concatenating issues from a second Ocre.
  *
  * @param ocre Ocre to concatenate with this Ocre.
  */
  def ++ (ocre : Ocre) : Ocre = {
    Ocre(issues ++ ocre.issues)
  }


  /** Create an ocho2 Corpus of coin legends.
  */
  def corpus: Corpus = {
    Corpus(issues.map(_.textNodes).flatten)
  }

  // useful for testing
  def take(n: Int): Ocre = {
    Ocre(issues.take(n), mintsGeo)
  }

  /** Create a new [[Ocre]] including only issues that have a denomination value.
  */
  def hasDenomination: Ocre = {
    val denomIssues = issues.filter(issue => (issue.denomination.trim != "none") && ! issue.denomination.trim.contains("uncertain"))
    Ocre(denomIssues)
  }

  /** Create a new [[Ocre]] including only issues that have a value for material.
  */
  def hasMaterial: Ocre = {
    val materialIssues = issues.filter(issue => (issue.material.trim != "none"))
    Ocre(materialIssues)
  }

  /** Create a new [[Ocre]] including only issues that have a value for authority.
  */
  def hasAuthority: Ocre = {
    val authIssues = issues.filter(issue => (issue.authority.trim != "none") && ! issue.authority.trim.contains("uncertain"))
    Ocre(authIssues)
  }

  /** Create a new [[Ocre]] including only issues that have a value for mint.
  */
  def hasMint: Ocre = {
    val mintIssues = issues.filter(issue => (issue.mint.trim != "none") && ! issue.mint.trim.contains("uncertain"))
    Ocre(mintIssues)
  }

  /** Create a new [[Ocre]] including only issues that have a value for region.
  */
  def hasRegion: Ocre = {
    val regionIssues = issues.filterNot(_.region.contains("uncertain")).filterNot(_.region.contains("none"))
    Ocre(regionIssues)
  }

  /** Create a new [[Ocre]] including only issues that have a value for obverse portrait ID.
  */
  def hasObvPortraitId: Ocre = {
    val oPortIssues = issues.filter(_.obvPortraitId.nonEmpty)
    Ocre(oPortIssues)
  }

  /** Create a new [[Ocre]] including only issues that have a value for obverse legend.
  */
  def hasObvLegend: Ocre = {
    val oLegendIssues = issues.filter(_.obvLegend.nonEmpty)
    Ocre(oLegendIssues)
  }


  /** Create a new [[Ocre]] including only issues that have a value for obverse type.
  */
  def hasObvType: Ocre = {
    val oTypeIssues = issues.filter(_.obvType.nonEmpty)
    Ocre(oTypeIssues)
  }


  /** Create a new [[Ocre]] including only issues that have a value for reverse legend.
  */
  def hasRevLegend: Ocre = {
    val rLegendIssues = issues.filter(_.revLegend.nonEmpty)
    Ocre(rLegendIssues)
  }

  /** Create a new [[Ocre]] including only issues that have a value for reverse type.
  */
  def hasRevType: Ocre = {
    val rTypeIssues = issues.filter(_.revType.nonEmpty)
    Ocre(rTypeIssues)
  }

  /** Create a new [[Ocre]] including only issues that have a value for reverse portrait ID.
  */
  def hasRevPortraitId: Ocre = {
    val rPortIssues = issues.filter(_.revPortraitId.nonEmpty)
    Ocre(rPortIssues)
  }


  /** Create a new [[Ocre]] including only issues that have a value for date range.
  */
  def datable: Ocre = {
    val datedIssues = issues.filter(_.dateRange != None)
    Ocre(datedIssues)
  }

  /** Group issues by issuing authority and sort chronologically.*/
  def byAuthority : Vector[(String, Ocre)] = {
    val byAuth = issues.groupBy(_.authority).map {
      case (auth, issues) => auth -> Ocre(issues)
    }
    byAuth.toVector.sortBy{ _._2.dateRange.pointAverage}
  }


  def issuesForAuthority(auth: String) : Vector[OcreIssue] = {
    val authNames = byAuthority.map(_._1)
    val idx = authNames.indexOf(auth)
    debug(auth + " == " + idx)
    if (idx < 0) {
      Vector.empty[OcreIssue]
    } else {
      val authOcres = byAuthority.map(_._2)
      authOcres(idx).issues
    }
  }

}


/** Factory object for making [[Ocre]]s. */
object Ocre {

  /** Construct an [[Ocre]] collection a single string
  * of CEX source.
  *
  * @param cex String of delimited-text data.
  * @param dropHeader True if cex includes a header line.
  */
  def apply(cex: String, dropHeader: Boolean) : Ocre = {
    val lines = cex.split("\n").toVector
    dropHeader match {
      case true => fromCexStrings(lines.tail)
      case false =>fromCexStrings(lines)
    }
  }

  /** Construct an [[Ocre]] collection CEX from a Vector
  * of text-delimited lines.
  *
  * @param cex String of delimited-text data.
  */
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
