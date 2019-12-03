package edu.holycross.shot.nomisma

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.histoutils._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

import scala.scalajs.js.annotation._

/** The contents of an edition of Rrco.
*
* @param issues
* @param mintsGeo
*/
@JSExportTopLevel("Rrco")
case class Rrco(
  issues:  Vector[RrcoIssue],
  mintsGeo: MintPointCollection = MintPointCollection(Vector.empty[MintPoint])
) extends IssueCollection with LogSupport {


  // useful for testing
  def take(n: Int): Rrco = {
    Rrco(issues.take(n), mintsGeo)
  }

  def hasDenomination: Rrco = {
    val denomIssues = issues.filter(issue => (issue.denomination.trim != "none") && ! issue.denomination.trim.contains("uncertain"))
    Rrco(denomIssues)
  }


  def hasMaterial: Rrco = {
    val materialIssues = issues.filter(issue => (issue.material.trim != "none"))
    Rrco(materialIssues)
  }


  def hasAuthority: Rrco = {
    val authIssues = issues.filter(issue => (issue.authority.trim != "none") && ! issue.authority.trim.contains("uncertain"))
    Rrco(authIssues)
  }


  def hasMint: Rrco = {
    val mintIssues = issues.filter(issue => (issue.mint.trim != "none") && ! issue.mint.trim.contains("uncertain"))
    Rrco(mintIssues)
  }


  def hasRegion: Rrco = {
    val regionIssues = issues.filterNot(_.region.contains("uncertain")).filterNot(_.region.contains("none"))
    Rrco(regionIssues)
  }

  def hasObvPortraitId: Rrco = {
    val oPortIssues = issues.filter(_.obvPortraitId.nonEmpty)
    Rrco(oPortIssues)
  }

  def hasObvLegend: Rrco = {
    val oLegendIssues = issues.filter(_.obvLegend.nonEmpty)
    Rrco(oLegendIssues)
  }

  def hasObvType: Rrco = {
    val oTypeIssues = issues.filter(_.obvType.nonEmpty)
    Rrco(oTypeIssues)
  }

  def hasRevLegend: Rrco = {
    val rLegendIssues = issues.filter(_.revLegend.nonEmpty)
    Rrco(rLegendIssues)
  }

  def hasRevType: Rrco = {
    val rTypeIssues = issues.filter(_.revType.nonEmpty)
    Rrco(rTypeIssues)
  }

  def hasRevPortraitId: Rrco = {
    val rPortIssues = issues.filter(_.revPortraitId.nonEmpty)
    Rrco(rPortIssues)
  }

  def datable: Rrco = {
    val datedIssues = issues.filter(_.dateRange != None)
    Rrco(datedIssues)
  }

  def byAuthority : Map[String, Rrco] = {
    val auths = issues.map(_.authority)
    val authMap = for (auth <- auths) yield {
      val subset = issues.filter(_.authority == auth)
      (auth -> Rrco(subset))
    }
    authMap.toMap
  }
}


/** Factory object for making [[Rrco]]s. */
object Rrco {

  /** Construct an [[Rrco]] collection a single string
  * of CEX source.
  *
  * @param cex String of delimited-text data.
  * @param dropHeader True if cex includes a header line.
  */
  def apply(cex: String, dropHeader: Boolean) : Rrco = {
    val lines = cex.split("\n").toVector
    dropHeader match {
      case true => fromCexStrings(lines.tail)
      case false =>fromCexStrings(lines)
    }
  }

  /** Construct an [[Rrco]] collection CEX from a Vector
  * of text-delimited lines.
  *
  * @param cex String of delimited-text data.
  */
  def fromCexStrings(cexLines : Vector[String]) : Rrco = {
    val issues = cexLines.map(RrcoIssue(_))
    Rrco(issues)
  }

  /** Create a new [[Rrco]] instance by associating a [[MintPointCollection]]
  * with the Vector of [[RrcoIssue]]s in an existing [[Rrco]].
  *
  * @param rrco Existing [[Rrco]] with [[RrcoIssue]]s to use.
  * @param geo [[MintPointCollection]] to use.
  */
  def addGeo(rrco: Rrco, geo: MintPointCollection): Rrco = {
    Rrco(rrco.issues, geo)
  }

}
