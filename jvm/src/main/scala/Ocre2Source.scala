package edu.holycross.shot.nomisma

import scala.io.Source
import java.io.File

import scala.xml._


/**
*/
object Ocre2Source {

  /** Parse RDF into an Ocre2 object.
  *
  * @param ocre Root of parsed OCRE data set.
  */
  def parseRdf(ocre: scala.xml.Elem): Ocre2 = {
    val descrs = ocre \\ "Description"
    val dv = descrs.toVector
    val legends = Legend.legendVector(dv)


    val typeDescrNodes = dv.filter( d => (d  \\ "description").size > 0 )
    val typeDescriptions =  TypeDescription.typeDescriptionVector(typeDescrNodes)


    val portraitElems = dv.filter( d => (d  \\ "hasPortrait").size > 0 )
    val portraits = Portrait.portraitVector(portraitElems)

    val issues = BasicIssue.parseOcreXml(ocre)

    Ocre2(issues, legends, typeDescriptions, portraits, MintPointCollection(Vector.empty[MintPoint]))
  }
}
