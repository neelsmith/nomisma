package edu.holycross.shot.nomisma


import scala.scalajs.js.annotation._
/** The contents of an edition of OCRE.
*
* @param issues
* @param legends
* @param typeDescriptions
* @param portraits
*/
@JSExportTopLevel("Ocre")
case class Ocre(
  issues:  Vector[OcreIssue],
  mintsGeo: MintPointCollection
) {


  def kml : String = {
    mintsGeo.forMints(issues.map(_.mint)).mintPoints.map(_.toKml).mkString("\n\n")
  }
}


object Ocre {
  /** Create a new [[Ocre]] instance by associating a [[MintPointCollection]]
  * with the Vector of [[OcreIssue]]s in an existing [[Ocre]].
  *
  * @param ocre Existing [[Ocre]] with [[OcreIssue]]s to use.
  * @param geo [[MintPointCollection]] to use.
  */
  def addGeo(ocre: Ocre, geo: MintPointCollection): Ocre = {
    Ocre(ocre.issues, geo)
  }
}
