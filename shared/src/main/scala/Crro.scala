package edu.holycross.shot.nomisma

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.histoutils._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

import scala.scalajs.js.annotation._

/** The contents of an edition of Crro.
*
* @param issues
* @param mintsGeo
*/
@JSExportTopLevel("Crro")
case class Crro(
  issues:  Vector[CrroIssue],
  mintsGeo: MintPointCollection = MintPointCollection(Vector.empty[MintPoint])
) extends IssueCollection with LogSupport {


  def ++ (crro : Crro) : Crro = {
    Crro(issues ++ crro.issues)
  }

  // useful for testing
  def take(n: Int): Crro = {
    Crro(issues.take(n), mintsGeo)
  }

  def hasDenomination: Crro = {
    val denomIssues = issues.filter(issue => (issue.denomination.trim != "none") && ! issue.denomination.trim.contains("uncertain"))
    Crro(denomIssues)
  }


  def hasMaterial: Crro = {
    val materialIssues = issues.filter(issue => (issue.material.trim != "none"))
    Crro(materialIssues)
  }


  def hasAuthority: Crro = {
    val authIssues = issues.filter(issue => (issue.authority.trim != "none") && ! issue.authority.trim.contains("uncertain"))
    Crro(authIssues)
  }


  def hasMint: Crro = {
    val mintIssues = issues.filter(issue => (issue.mint.trim != "none") && ! issue.mint.trim.contains("uncertain"))
    Crro(mintIssues)
  }


  def hasRegion: Crro = {
    val regionIssues = issues.filterNot(_.region.contains("uncertain")).filterNot(_.region.contains("none"))
    Crro(regionIssues)
  }

  def hasObvPortraitId: Crro = {
    val oPortIssues = issues.filter(_.obvPortraitId.nonEmpty)
    Crro(oPortIssues)
  }

  def hasObvLegend: Crro = {
    val oLegendIssues = issues.filter(_.obvLegend.nonEmpty)
    Crro(oLegendIssues)
  }

  def hasObvType: Crro = {
    val oTypeIssues = issues.filter(_.obvType.nonEmpty)
    Crro(oTypeIssues)
  }

  def hasRevLegend: Crro = {
    val rLegendIssues = issues.filter(_.revLegend.nonEmpty)
    Crro(rLegendIssues)
  }

  def hasRevType: Crro = {
    val rTypeIssues = issues.filter(_.revType.nonEmpty)
    Crro(rTypeIssues)
  }

  def hasRevPortraitId: Crro = {
    val rPortIssues = issues.filter(_.revPortraitId.nonEmpty)
    Crro(rPortIssues)
  }

  def datable: Crro = {
    val datedIssues = issues.filter(_.dateRange != None)
    Crro(datedIssues)
  }

  def byAuthority : Map[String, Crro] = {
    val auths = issues.map(_.authority)
    val authMap = for (auth <- auths) yield {
      val subset = issues.filter(_.authority == auth)
      (auth -> Crro(subset))
    }
    authMap.toMap
  }
}


/** Factory object for making [[Crro]]s. */
object Crro {

  /** Construct an [[Crro]] collection a single string
  * of CEX source.
  *
  * @param cex String of delimited-text data.
  * @param dropHeader True if cex includes a header line.
  */
  def apply(cex: String, dropHeader: Boolean) : Crro = {
    val lines = cex.split("\n").toVector
    dropHeader match {
      case true => fromCexStrings(lines.tail)
      case false =>fromCexStrings(lines)
    }
  }

  /** Construct an [[Crro]] collection CEX from a Vector
  * of text-delimited lines.
  *
  * @param cex String of delimited-text data.
  */
  def fromCexStrings(cexLines : Vector[String]) : Crro = {
    val issues = cexLines.map(CrroIssue(_))
    Crro(issues)
  }

  /** Create a new [[Crro]] instance by associating a [[MintPointCollection]]
  * with the Vector of [[CrroIssue]]s in an existing [[Crro]].
  *
  * @param rrco Existing [[Crro]] with [[CrroIssue]]s to use.
  * @param geo [[MintPointCollection]] to use.
  */
  def addGeo(rrco: Crro, geo: MintPointCollection): Crro = {
    Crro(rrco.issues, geo)
  }

}
