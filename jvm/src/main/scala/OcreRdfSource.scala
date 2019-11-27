package edu.holycross.shot.nomisma

import scala.io.Source
import java.io.File

import scala.xml._


/**
*/
object OcreRdfSource {

  /** Parse RDF into an OcreRdf object.
  *
  * @param ocre Root of parsed OCRE data set.
  */
  def parseRdf(ocre: scala.xml.Elem): OcreRdf = {
    val descrs = ocre \\ "Description"
    val dv = descrs.toVector
    val legends = Legend.legendVector(dv)


    val typeDescrNodes = dv.filter( d => (d  \\ "description").size > 0 )
    val typeDescriptions =  TypeDescription.typeDescriptionVector(typeDescrNodes)


    val portraitElems = dv.filter( d => (d  \\ "hasPortrait").size > 0 )
    val portraits = Portrait.portraitVector(portraitElems)

    val issues = BasicIssue.parseOcreXml(ocre)

    OcreRdf(issues, legends, typeDescriptions, portraits, MintPointCollection(Vector.empty[MintPoint]))
  }
}
