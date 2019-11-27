import scala.xml._
import java.io.PrintWriter
import edu.holycross.shot.nomisma._

// OCRE is enormous.  Start sbt with lot of memory, e.g.,
// SBT_OPTS="-Xms512M -Xmx4096M -XX:MaxMetaspaceSize=1024M" sbt

// What this script can do:
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

def sideForString(s: String): CoinSide = {
  s.toLowerCase match {
    case "reverse" => Reverse
    case "obverse" => Obverse
    case sideLabel: String => throw new Exception("Unrecognized value for coin side: " + sideLabel)
  }
}

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
/*
case class Ocre(issue:  Vector[BasicIssue], legends: Vector[Legend], typeDescriptions: Vector[TypeDescription], portraits: Vector[Portrait])
*/


/** Given a Vector of OCRE TypeDescription nodes,
* return a Vector of [[Legend]]s.
*
* @param descriptionV Vector of OCRE TypeDescription nodes.
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
      Some(Legend(triple(0), sideForString(triple(1)), triple(2))  )
    } else {
      println("Struck out on " + l)
      None
    }
  }
   legendsRaw.filter(_.isDefined).map(_.get)
}


/** Given a Vector of OCRE TypeDescription nodes,
* return a Vector of [[TypeDescription]]s.
*
* @param descriptionV Vector of OCRE Description nodes.
*/
def typeDescriptionVector(typeDescrs: Vector[scala.xml.Node]) : Vector[TypeDescription] = {
  val descriptionText = for (d <-typeDescrs) yield {
    val rdgs = d \\ "description"
    val rdg = rdgs(0)
    d.attributes.value.toString + "#" + rdg.text
  }
  val descrsRaw = for (d <- descriptionText) yield {
    val triple = d.split("#")
    if (triple.size == 3) {
      Some(TypeDescription(triple(0), sideForString(triple(1)), triple(2))  )
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
      //println("COIN SIDE " + coinSide.toVector)
      //println("ATTRS "  + rdgs(0).attributes)
      Some(Portrait(coinSide(0), sideForString(coinSide(1)), rdgs(0).attributes.toString))//value ))

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
def parseOcre(ocre: scala.xml.Elem): Ocre2 = {
  val descrs = ocre \\ "Description"
  val dv = descrs.toVector
  val legends = legendVector(dv)


  val typeDescrNodes = dv.filter( d => (d  \\ "description").size > 0 )
  val typeDescriptions =  typeDescriptionVector(typeDescrNodes)


  val portraitElems = dv.filter( d => (d  \\ "hasPortrait").size > 0 )
  val portraits = portraitVector(portraitElems)

  val issues = parseBasicOcre(ocre)

  Ocre2(issues, legends, typeDescriptions, portraits, MintPointCollection(Vector.empty))
}

def loadAndParse(fName: String): Ocre2 = {
  val root = loadOcre(fName)
  parseOcre(root)
}


def info = {
  println("Things you can do with this script:\n")

  println("\tloadOcre(fName: String): scala.xml.Elem")
  println("\tparseOcre(ocre: scala.xml.Elem): Ocre2")
  println("\tloadAndParse(fName: String): Ocre2")
  println("\nTo see this message:\n\tinfo")
}


println("\n\n ")
info
