package edu.holycross.shot.nomisma

import scala.io.Source
import java.io.File
import scala.xml._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

/**
*/
object CrroSource extends LogSupport {


  /** Parse data in CEX format into a Crro.
  *
  * @param cexLines Strings of data in CEX format.
  * @param dropHeader True if CEX has a header line.
  */
  def fromCex(cexLines: Vector[String], dropHeader:  Boolean = true) : Crro = {
    info("Reading " + cexLines.size + " lines of CEX data.")
    val issues = for (ln <- cexLines) yield {
      val cols = ln.split("#")
      if (cols.size >= 15) {
        val issue = CrroIssue(ln)
        Some(issue)
      } else {
        warn("FAILED ON " + ln + s"\nOnly found ${cols.size} columns")
        None
      }
    }
    val crro = Crro(issues.toVector.flatten)
    info("Created Crro with " + crro.issues.size + " issues.")
    crro
  }

  /** Parse data in CEX format into a Crro.
  *
  * @param fName File with data in CEX format.
  * @param dropHeader True if CEX file has a header line.
  */
  def fromFile(fName: String, dropHeader: Boolean = true): Crro = {
    val cex = if (dropHeader) {
      Source.fromFile(fName).getLines.toVector.tail
    } else {
      Source.fromFile(fName).getLines.toVector
    }
    fromCex(cex,dropHeader)
  }


  /** Parse data in CEX format into a Crro.
  *
  * @param urlString File with data in CEX format.
  * @param dropHeader True if CEX file has a header line.
  */
  def fromUrl(urlString: String, dropHeader: Boolean = true)  : Crro = {
    val cex = if (dropHeader) {
      Source.fromURL(urlString).getLines.toVector.tail
    } else {
      Source.fromURL(urlString).getLines.toVector
    }
    fromCex(cex,dropHeader)
  }

  /** Parse a file of data in RDF format into a Crro.
  *
  * @param fName File with data in RDF format.
  * @param msgInterval Frequency of logging info when parsing.
  */
  def fromRdfFile(fName: String, msgInterval: Int = 100): Crro = {
    val root = XML.loadFile(fName)
    val crroRdf = NomismaRdfCollection.parseRdf(root)
    crroRdf.toCrro(msgInterval)
  }

  /** Parse a URL with data in RDF format into a Crro.
  *
  * @param url Data in RDF format.
  * @param msgInterval Frequency of logging info when parsing.
  */
  def fromRdfUrl(urlString : String = "http://numismatics.org/crro/nomisma.rdf", msgInterval: Int = 100) : Crro = {
      val root = XML.load(urlString)
      val crroRdf = NomismaRdfCollection.parseRdf(root)
      crroRdf.toCrro(msgInterval)
  }
}
