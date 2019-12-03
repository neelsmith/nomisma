package edu.holycross.shot.nomisma

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.histoutils._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

import scala.scalajs.js.annotation._

/** The contents of a catalog of issues such as
* OCRE or CRRO.
*
* @param issues
* @param mintsGeo
*/
@JSExportTopLevel("GenericIssuesCatalog")
case class GenericIssuesCatalog(
  issues:  Vector[NomismaIssue],
  mintsGeo: MintPointCollection = MintPointCollection(Vector.empty[MintPoint])
) extends IssueCollection with LogSupport {

  // composite a second catalog with this one
  def ++(catalog2: IssueCollection) = {
    GenericIssuesCatalog(issues ++ catalog2.issues, mintsGeo)
  }
  // useful for testing
  def take(n: Int): GenericIssuesCatalog = {
    GenericIssuesCatalog(issues.take(n), mintsGeo)
  }

  def hasDenomination: GenericIssuesCatalog = {
    val denomIssues = issues.filter(issue => (issue.denomination.trim != "none") && ! issue.denomination.trim.contains("uncertain"))
    GenericIssuesCatalog(denomIssues)
  }


  def hasMaterial: GenericIssuesCatalog = {
    val materialIssues = issues.filter(issue => (issue.material.trim != "none"))
    GenericIssuesCatalog(materialIssues)
  }


  def hasAuthority: GenericIssuesCatalog = {
    val authIssues = issues.filter(issue => (issue.authority.trim != "none") && ! issue.authority.trim.contains("uncertain"))
    GenericIssuesCatalog(authIssues)
  }


  def hasMint: GenericIssuesCatalog = {
    val mintIssues = issues.filter(issue => (issue.mint.trim != "none") && ! issue.mint.trim.contains("uncertain"))
    GenericIssuesCatalog(mintIssues)
  }


  def hasRegion: GenericIssuesCatalog = {
    val regionIssues = issues.filterNot(_.region.contains("uncertain")).filterNot(_.region.contains("none"))
    GenericIssuesCatalog(regionIssues)
  }

  def hasObvPortraitId: GenericIssuesCatalog = {
    val oPortIssues = issues.filter(_.obvPortraitId.nonEmpty)
    GenericIssuesCatalog(oPortIssues)
  }

  def hasObvLegend: GenericIssuesCatalog = {
    val oLegendIssues = issues.filter(_.obvLegend.nonEmpty)
    GenericIssuesCatalog(oLegendIssues)
  }

  def hasObvType: GenericIssuesCatalog = {
    val oTypeIssues = issues.filter(_.obvType.nonEmpty)
    GenericIssuesCatalog(oTypeIssues)
  }

  def hasRevLegend: GenericIssuesCatalog = {
    val rLegendIssues = issues.filter(_.revLegend.nonEmpty)
    GenericIssuesCatalog(rLegendIssues)
  }

  def hasRevType: GenericIssuesCatalog = {
    val rTypeIssues = issues.filter(_.revType.nonEmpty)
    GenericIssuesCatalog(rTypeIssues)
  }

  def hasRevPortraitId: GenericIssuesCatalog = {
    val rPortIssues = issues.filter(_.revPortraitId.nonEmpty)
    GenericIssuesCatalog(rPortIssues)
  }

  def datable: GenericIssuesCatalog = {
    val datedIssues = issues.filter(_.dateRange != None)
    GenericIssuesCatalog(datedIssues)
  }

  def byAuthority : Map[String, GenericIssuesCatalog] = {
    val auths = issues.map(_.authority)
    val authMap = for (auth <- auths) yield {
      val subset = issues.filter(_.authority == auth)
      (auth -> GenericIssuesCatalog(subset))
    }
    authMap.toMap
  }
}


/** Factory object for making [[GenericIssuesCatalog]]s. */
object GenericIssuesCatalog {

  /** Construct an [[GenericIssuesCatalog]] collection a single string
  * of CEX source.
  *
  * @param cex String of delimited-text data.
  * @param dropHeader True if cex includes a header line.
  */
  def apply(cex: String, dropHeader: Boolean) : GenericIssuesCatalog = {
    val lines = cex.split("\n").toVector
    dropHeader match {
      case true => fromCexStrings(lines.tail)
      case false =>fromCexStrings(lines)
    }
  }

  /** Construct an [[GenericIssuesCatalog]] collection CEX from a Vector
  * of text-delimited lines.
  *
  * @param cex String of delimited-text data.
  */
  def fromCexStrings(cexLines : Vector[String]) : GenericIssuesCatalog = {
    val issues = cexLines.map(GenericIssue(_))
    GenericIssuesCatalog(issues)
  }

  /** Create a new [[GenericIssuesCatalog]] instance by associating a [[MintPointCollection]]
  * with the Vector of [[NomismaIssue]]s in an existing [[GenericIssuesCatalog]].
  *
  * @param ocre Existing [[GenericIssuesCatalog]] with [[NomismaIssue]]s to use.
  * @param geo [[MintPointCollection]] to use.
  */
  def addGeo(ocre: GenericIssuesCatalog, geo: MintPointCollection): GenericIssuesCatalog = {
    GenericIssuesCatalog(ocre.issues, geo)
  }

}
