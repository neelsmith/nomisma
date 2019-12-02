package edu.holycross.shot.nomisma

import scala.io.Source
import java.io.File
import scala.xml._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

/**
*/
object UrlManagerSource extends LogSupport {


  /** Parse data in CEX format into an Ocre.
  *
  * @param fName File with data in CEX format.
  */
  def fromFile(fName: String, delimiter: String = "\t"): UrlManager = {
    val pairings = Source.fromFile(fName).getLines.mkString("\n")
    UrlManager(pairings, delimiter)
  }


  /** Parse data in CEX format into an Ocre.
  *
  * @param urlString File with data in CEX format.
  */
  def fromUrl(urlString: String, delimiter: String = "\t")  : UrlManager = {
    val pairings = Source.fromURL(urlString).getLines.mkString("\n")
    UrlManager(pairings, delimiter)
  }

}
