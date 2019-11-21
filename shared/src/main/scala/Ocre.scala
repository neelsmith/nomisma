package edu.holycross.shot.nomisma


/** The contents of an edition of OCRE.
*
* @param issues
* @param legends
* @param typeDescriptions
* @param portraits
*/
case class Ocre(
  issues:  Vector[BasicIssue],
  legends: Vector[Legend],
  typeDescriptions: Vector[TypeDescription],
  portraits: Vector[Portrait],
  mintsGeo: MintPointCollection
) {


}


object Ocre {


  def addGeo(ocre: Ocre, geo: MintPointCollection): Ocre = {
    Ocre(ocre.issues, ocre.legends, ocre.typeDescriptions, ocre.portraits, geo)
  }
  /**
  *
  * @param ocre Root of parsed OCRE data set.
  */
  def parseRdf(ocre: scala.xml.Elem): Ocre = {
    val descrs = ocre \\ "Description"
    val dv = descrs.toVector
    val legends = Legend.legendVector(dv)


    val typeDescrNodes = dv.filter( d => (d  \\ "description").size > 0 )
    val typeDescriptions =  TypeDescription.typeDescriptionVector(typeDescrNodes)


    val portraitElems = dv.filter( d => (d  \\ "hasPortrait").size > 0 )
    val portraits = Portrait.portraitVector(portraitElems)

    val issues = BasicIssue.parseOcreXml(ocre)

    Ocre(issues, legends, typeDescriptions, portraits, MintPointCollection(Vector.empty[MintPoint]))

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
