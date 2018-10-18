import scala.xml._
import java.io.PrintWriter

// OCRE is enormous.  Start scala with lot of memory, e.g.,
//     scala -J-Xms256m -J-Xmx4096m


// What this can do:
// 1.  read OCRE RDF
// 2.  Extract for each issue obverse type, obverse legend, obverse portrait, reverse type, reverse legend, reverse portrait
// 3.  Join these issue records to a previously extracted set of records with basic data about the issue


/** Load OCRE XML from a named file.
*
* @param fName Name of file to load.
*/
def loadOcre(fName: String): scala.xml.Elem = {
  println("Loading OCRE data...")
  XML.loadFile(fName)
}


/**  A coin legend.
*
* @param coin
* @param side
* @param legend
*/
case class Legend (coin: String, side: String, legend: String)

/**
*/
case class Description (coin: String, side: String, typeDesc: String)

/**
*/
case class Portrait (coin:String, side: String, portrait: String)


case class BasicIssue(id: String, label:  String,
denomination: String, material: String, authority: String, mint: String, region: String)


/** Parse basic OCRE Issue data from RDF.
*
* @param ocre Root of parsed OCRE RDF.
*/
def parseBasicOcre(ocre: scala.xml.Elem) : Vector[BasicIssue] = {
  val typeSeries = ocre \\ "TypeSeriesItem"
  val typesVect = typeSeries.toVector

  val data = for (t <- typesVect) yield {
    val lab = (t \\ "prefLabel")(0).text


    val denomElems = (t \\ "hasDenomination")
    val  denom =  if (denomElems.isEmpty) { "none" } else {     denomElems(0).attributes.value.toString.replaceFirst("http://nomisma.org/id/","")
    }


    val materialElems = (t \\ "hasMaterial")
    val  material =  if (materialElems.isEmpty) { "none" } else {     materialElems(0).attributes.value.toString.replaceFirst("http://nomisma.org/id/","")
    }



    val authorityElems = (t \\ "hasAuthority")
    val  authority =  if (authorityElems.isEmpty) { "none" } else {     authorityElems(0).attributes.value.toString.replaceFirst("http://nomisma.org/id/","")
    }



    val mintElems = (t \\ "hasMint")
    val  mint =  if (mintElems.isEmpty) { "none" } else {     mintElems(0).attributes.value.toString.replaceFirst("http://nomisma.org/id/","")
    }


    val regionElems = (t \\ "hasRegion")
    val  region =  if (regionElems.isEmpty) { "none" } else {     regionElems(0).attributes.value.toString.replaceFirst("http://nomisma.org/id/","")
    }


    val id =  t.attributes.value(0).toString.replaceFirst("http://numismatics.org/ocre/id/", "")
    //+ s"#${lab}#${denom}#${material}#${authority}#${mint}#${region}"
    BasicIssue(id, lab, denom, material, authority, mint, region)
  }
  data.toVector
}

case class Ocre(issue:  Vector[BasicIssue], legends: Vector[Legend], typeDescriptions: Vector[Description], portraits: Vector[Portrait])



/** Given a Vector of OCRE Description nodes,
* return a Vector of [[Legend]]s.
*
* @param descriptionV Vector of OCRE Description nodes.
*/
def legendVector(descriptionV: Vector[scala.xml.Node]) : Vector[Legend] = {
  val legendsElems = descriptionV.filter( d => (d  \\ "hasLegend").size > 0 )
  val legendsText = for (l <-legendsElems) yield {
    val rdgs = l \\ "hasLegend"
    val rdg = rdgs(0)
    l.attributes.value.toString + "#" + rdg.text
  }
  val legendsRaw = for (l <- legendsText) yield {
    val triple = l.split("#")
    if (triple.size == 3) {
      Some(Legend(triple(0), triple(1), triple(2))  )
    } else {
      println("Struck out on " + l)
      None
    }
  }
   legendsRaw.filter(_.isDefined).map(_.get)
}


/** Given a Vector of OCRE Description nodes,
* return a Vector of [[Description]]s.
*
* @param descriptionV Vector of OCRE Description nodes.
*/
def typeDescriptionVector(typeDescrs: Vector[scala.xml.Node]) : Vector[Description] = {
  val descriptionText = for (d <-typeDescrs) yield {
    val rdgs = d \\ "description"
    val rdg = rdgs(0)
    d.attributes.value.toString + "#" + rdg.text
  }
  val descrsRaw = for (d <- descriptionText) yield {
    val triple = d.split("#")
    if (triple.size == 3) {
      Some(Description(triple(0), triple(1), triple(2))  )
    } else {
      println("Struck out on " + d)
      None
    }
  }
  descrsRaw.filter(_.isDefined).map(_.get)
}


/** Given a Vector of OCRE Description nodes,
* return a Vector of [[Portrait]]s.
*
* @param descriptionV Vector of OCRE Description nodes.
*/
def portraitVector(descriptionV: Vector[scala.xml.Node]) : Vector[Portrait] = {
  val portraits = for (p <- descriptionV) yield {
    val rdgs = p \\ "hasPortrait"
    // p.attributes.value.toString is BOTH
    // coin ID and side!
    if (rdgs.nonEmpty) {
      val coinSide = p.attributes.value.toString.split("#")
      println("COIN SIDE " + coinSide.toVector)
      println("ATTRS "  + rdgs(0).attributes)
      Some(Portrait(coinSide(0), coinSide(1), rdgs(0).attributes.toString))//value ))

    } else {
      None
    }
  }
  portraits.flatten
/*
  val portraitsRaw = for (p <- portraitText.flatten) yield {
    val triple = p.split("#")
    if (triple.size == 3) {
      Some(Portrait(triple(0), triple(1), triple(2))  )
    } else {
      println("Struck out on " + p)
      None
    }
  }
  portraitsRaw.filter(_.isDefined).map(_.get)
  */
}



/**
*
* @param ocre Root of parsed OCRE data set.
*/
def parseOcre(ocre: scala.xml.Elem): Ocre = {
  val descrs = ocre \\ "Description"
  val dv = descrs.toVector
  val legends = legendVector(dv)


  val typeDescrNodes = dv.filter( d => (d  \\ "description").size > 0 )
  val typeDescriptions =  typeDescriptionVector(typeDescrNodes)


  val portraitElems = dv.filter( d => (d  \\ "hasPortrait").size > 0 )
  val portraits = portraitVector(portraitElems)

  val issues = parseBasicOcre(ocre)

  Ocre(issues, legends, typeDescriptions, portraits)

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
