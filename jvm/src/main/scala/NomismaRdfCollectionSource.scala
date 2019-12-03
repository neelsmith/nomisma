package edu.holycross.shot.nomisma

import scala.io.Source
import java.io.File

import scala.xml._


/**
*/
object NomismaRdfCollectionSource {


  /** Parse a file of nomisma.org's RDF data into a [[NomismaRdfCollection]] object.
  *
  * @param fName Name of file.
  */
  def fromFile(fName: String): NomismaRdfCollection = {
    val root = XML.loadFile(fName)
    parseRdf(root)
  }

  /** Parse RDF into an [[NomismaRdfCollection]] object.
  *
  * @param ocre Root of parsed OCRE data set.
  */
  def parseRdf(ocre: scala.xml.Elem): NomismaRdfCollection = {
    val descrs = ocre \\ "Description"
    val dv = descrs.toVector
    val legends = Legend.legendVector(dv)

    val typeDescrNodes = dv.filter( d => (d  \\ "description").size > 0 )
    val typeDescriptions =  TypeDescription.typeDescriptionVector(typeDescrNodes)

    val portraitElems = dv.filter( d => (d  \\ "hasPortrait").size > 0 )
    val portraits = Portrait.portraitVector(portraitElems)

    val dateRanges = Vector.empty[IssueYearRange] // NOT YET IMPLEMENTED


    val issues = BasicIssue.parseOcreXml(ocre)


    NomismaRdfCollection(issues, legends, typeDescriptions, portraits, dateRanges, MintPointCollection(Vector.empty[MintPoint]))
  }
}
