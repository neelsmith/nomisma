package edu.holycross.shot.nomisma


import scala.scalajs.js.annotation._
/** The contents of an edition of OCRE.
*
* @param issues
* @param legends
* @param typeDescriptions
* @param portraits
*/
@JSExportTopLevel("Ocre2")
case class Ocre2(
  issues:  Vector[BasicIssue],
  legends: Vector[Legend],
  typeDescriptions: Vector[TypeDescription],
  portraits: Vector[Portrait],
  mintsGeo: MintPointCollection
) {


  def ocreIssue(id: String) : Option[OcreIssue] = {
    val issueMatches = issues.filter(_.id == id)
     issueMatches.size  match {
      case 1 => {
        val basics = issueMatches(0)
        val geo = mintsGeo.forMint(basics.mint)

        val rTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Reverse)
        val rType = if (rTypeMatches.size == 1) {
          rTypeMatches(0).description
        } else {""}

        val oTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Obverse)
        val oType = if (oTypeMatches.size == 1) {
          oTypeMatches(0).description
        } else {""}

        val rLegendMatches = legends.filter(_.coin == id).filter(_.side == Reverse)
        val rLegend = if (rLegendMatches.size == 1) {
          rLegendMatches(0).legend
        } else {""}

        val oLegendMatches = legends.filter(_.coin == id).filter(_.side == Obverse)
        val oLegend = if (oLegendMatches.size == 1) {
          oLegendMatches(0).legend
        } else {""}

        val oPortrait = "" // not yet implemented
        val rPortrait = "" // not yet implemented
        val dateRange = None // not yet implemented

        Some(OcreIssue(basics.id, basics.labelText, basics.denomination, basics.material, basics.authority, basics.mint, basics.region, oType, oLegend, oPortrait, rType, rLegend,  rPortrait, dateRange))
      }
      case _ => None
    }
  }


  def ocreIssues(ids: Vector[String]): Vector[OcreIssue] = {
    val opts = ids.map(ocreIssue(_))
    opts.flatten
  }

  def ocreIssue2s(ids: Vector[String]): Vector[OcreIssue2] = {
    val opts = ids.map(ocreIssue2(_))
    opts.flatten
  }

  /** Given an object identifier, construct an [[OcreIssue2]].
  *
  * @param id Object identifier for issue.
  */
  def ocreIssue2(id: String) : Option[OcreIssue2] = {
    val issueMatches = issues.filter(_.id == id)
     issueMatches.size  match {
      case 1 => {
        val basics = issueMatches(0)
        val geo = mintsGeo.forMint(basics.mint)

        val rTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Reverse)
        val rType = if (rTypeMatches.size == 1) { Some(rTypeMatches(0))} else {None}

        val oTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Obverse)
        val oType = if (oTypeMatches.size == 1) { Some(oTypeMatches(0))} else {None}

        val rLegendMatches = legends.filter(_.coin == id).filter(_.side == Reverse)
        val rLegend = if (rLegendMatches.size == 1) { Some(rLegendMatches(0))} else {None}

        val oLegendMatches = legends.filter(_.coin == id).filter(_.side == Obverse)
        val oLegend = if (oLegendMatches.size == 1) { Some(oLegendMatches(0))} else {None}

        val oPortrait = None // not yet implemented
        val rPortrait = None // not yet implemented

        Some(OcreIssue2(basics, oType, rType, oLegend, rLegend, oPortrait, rPortrait, geo))
      }
      case _ => None
    }
  }


  def kml : String = {
    mintsGeo.forMints(issues.map(_.mint)).mintPoints.map(_.toKml).mkString("\n\n")
  }
}


object Ocre2 {


  def addGeo(ocre: Ocre2, geo: MintPointCollection): Ocre2 = {
    Ocre2(ocre.issues, ocre.legends, ocre.typeDescriptions, ocre.portraits, geo)
  }
  /**
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





  /*
  import scala.io.Source
  val typeData = Source.fromFile("ocre-types-uniq.cex").getLines.toVector.tail


  println("Mapping legends to issue data...")

  val legendary = for (t <- typeData) yield {
    val cols = t.split("#")
    val id = "http://numismatics.org/ocre/id/" + cols(0)
    val legendMatches = legends.filter(_.coin == id)

    val obvLegends = legendMatches.filter(_.side == "obverse")
    val revLegends = legendMatches.filter(_.side == "reverse")
    val olegend = if (obvLegends.nonEmpty)  { obvLegends(0).legend} else {""}
    val rlegend = if (revLegends.nonEmpty)  { revLegends(0).legend} else {""}

    val typeMatches = descriptions.filter(_.coin == id)
    val obvTypes = typeMatches.filter(_.side == "obverse")
    val revTypes = typeMatches.filter(_.side == "reverse")
    val oType = if (obvTypes.nonEmpty)  { obvTypes(0).typeDesc} else {""}
    val rType = if (revTypes.nonEmpty)  { revTypes(0).typeDesc} else {""}


    val portMatches = portraits.filter(_.coin == id)
    val obvPorts = portMatches.filter(_.side == "obverse")
    val revPorts = portMatches.filter(_.side == "reverse")
    val oPort = if (obvPorts.nonEmpty)  { obvPorts(0).portrait} else {""}
    val rPort = if (revPorts.nonEmpty)  { revPorts(0).portrait} else {""}


    val fullRecord = s"${t}#${oType}#${olegend}#${oPort}#${rType}#${rlegend}#${rPort}"
    println(fullRecord)
    fullRecord
  }
  println("total issues: " + legendary.size)



  new PrintWriter("ocre-composite.cex") {write(legendary.mkString("\n")); close;}

  */
}
