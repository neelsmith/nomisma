package edu.holycross.shot.nomisma

import scala.io.Source
import java.io.File
import scala.xml._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

/**
*/
object GenericIssuesCatalogSource extends LogSupport {


  /** Parse data in CEX format into a [[GenericIssuesCatalog]].
  *
  * @param cexLines Strings of data in CEX format.
  * @param dropHeader True if CEX has a header line.
  */
  def fromCex(cexLines: Vector[String], dropHeader:  Boolean = true) : GenericIssuesCatalog = {
    info("Reading " + cexLines.size + " lines of CEX data.")
    val issues = for (ln <- cexLines) yield {
      val cols = ln.split("#")
      if (cols.size >= 15) {
        val issue = GenericIssue(ln)
        Some(issue)
      } else {
        warn("FAILED ON " + ln + s"\nOnly found ${cols.size} columns")
        None
      }
    }
    val issuesCatalog = GenericIssuesCatalog(issues.toVector.flatten)
    info("Created GenericIssuesCatalog with " + issuesCatalog.issues.size + " issues.")
    issuesCatalog
  }

  /** Parse data in CEX format into a [[GenericIssuesCatalog]].
  *
  * @param fName File with data in CEX format.
  * @param dropHeader True if CEX file has a header line.
  */
  def fromFile(fName: String, dropHeader: Boolean = true): GenericIssuesCatalog = {
    val cex = if (dropHeader) {
      Source.fromFile(fName).getLines.toVector.tail
    } else {
      Source.fromFile(fName).getLines.toVector
    }
    fromCex(cex,dropHeader)
  }


  /** Parse data in CEX format into a [[GenericIssuesCatalog]].
  *
  * @param urlString File with data in CEX format.
  * @param dropHeader True if CEX file has a header line.
  */
  def fromUrl(urlString: String, dropHeader: Boolean = true)  : GenericIssuesCatalog = {
    val cex = if (dropHeader) {
      Source.fromURL(urlString).getLines.toVector.tail
    } else {
      Source.fromURL(urlString).getLines.toVector
    }
    fromCex(cex,dropHeader)
  }

  /** Parse a file of data in RDF format into a [[GenericIssuesCatalog]].
  *
  * @param fName File with data in RDF format.
  * @param msgInterval Frequency of logging info when parsing.
  */
  def fromRdfFile(fName: String, msgInterval: Int = 100)  : GenericIssuesCatalog = {
    val root = XML.loadFile(fName)
    val rdf = NomismaRdfCollection.parseRdf(root)
    rdf.toGenericCatalog(msgInterval)
  }

  /** Parse a URL with data in RDF format into a [[GenericIssuesCatalog]].
  *
  * @param url Data in RDF format.
  * @param msgInterval Frequency of logging info when parsing.
  */
  def fromRdfUrl(urlString : String, msgInterval: Int = 100) : GenericIssuesCatalog = {
      val root = XML.load(urlString)
      val rdf = NomismaRdfCollection.parseRdf(root)
      rdf.toGenericCatalog(msgInterval)
  }
}
