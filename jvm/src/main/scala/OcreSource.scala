package edu.holycross.shot.nomisma

import scala.io.Source
import java.io.File


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

/**
*/
object OcreSource extends LogSupport {

  /** Parse RDF into an Ocre2 object.
  *
  * @param ocre Root of parsed OCRE data set.
  */
  def fromFile(fName: String, dropHeader: Boolean = true): Ocre = {
    val cex = if (dropHeader) {
      Source.fromFile(fName).getLines.toVector.tail
    } else {
      Source.fromFile(fName).getLines.toVector
    }
    info("Reading " + cex.size + " lines of CEX data.")
    val issues = for (ln <- cex) yield {
      val cols = ln.split("#")
      if (cols.size >= 15) {
        val issue = OcreIssue(ln)
        Some(issue)
      } else {
        warn("FAILED ON " + ln + s"\nOnly found ${cols.size} columns")
        None
      }
    }
    val ocre = Ocre(issues.toVector.flatten)
    info("Created Ocre with " + ocre.issues.size + " issues.")
    ocre
  }
}
