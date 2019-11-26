package edu.holycross.shot.nomisma

import scala.io.Source
import java.io.File


/**
*/
object OcreSource {

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
    val issues = for (ln <- cex) yield {
      val cols = ln.split("#")
      if (cols.size >= 15) {
        val issue = OcreIssue(ln)
        Some(issue)
      } else {
        println("FAILED ON " + ln + s"\nOnly found ${cols.size} columns")
        None
      }
    }
    Ocre(issues.toVector.flatten)
  }
}
