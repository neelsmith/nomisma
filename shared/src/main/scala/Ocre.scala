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

  def hasDenomination: Ocre = {
    val denomIssues = issues.filter(issue => (issue.denomination.trim != "none") && ! issue.denomination.trim.contains("uncertain"))
    Ocre(denomIssues)
  }


  def hasMaterial: Ocre = {
    val materialIssues = issues.filter(issue => (issue.material.trim != "none"))
    Ocre(materialIssues)
  }


  def hasAuthority: Ocre = {
    val authIssues = issues.filter(issue => (issue.authority.trim != "none") && ! issue.authority.trim.contains("uncertain"))
    Ocre(authIssues)
  }


  def hasMint: Ocre = {
    val mintIssues = issues.filter(issue => (issue.mint.trim != "none") && ! issue.mint.trim.contains("uncertain"))
    Ocre(mintIssues)
  }


  def hasRegion: Ocre = {
    val regionIssues = issues.filterNot(_.region.contains("uncertain")).filterNot(_.region.contains("none"))
    Ocre(regionIssues)
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

  def hasRevPortraitId: Ocre = {
    val rPortIssues = issues.filter(_.revPortraitId.nonEmpty)
    Ocre(rPortIssues)
  }

  def datable: Ocre = {
    val datedIssues = issues.filter(_.dateRange != None)
    Ocre(datedIssues)
  }

  def byAuthority : Map[String, Ocre] = {
    val auths = issues.map(_.authority)
    val authMap = for (auth <- auths) yield {
      val subset = issues.filter(_.authority == auth)
      (auth -> Ocre(subset))
    }
    authMap.toMap
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
