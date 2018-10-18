import scala.xml._
import java.io.PrintWriter

// OCRE is enormous.  Start scala with lot of memory, e.g.,
//     scala -J-Xms256m -J-Xmx4096m

/** Load OCRE XML from a named file, e.g.,
*  loadOcre("ocre.rdf")
*
* @param fName Name of file to load.
*/
def loadOcre(fName: String): scala.xml.Elem = {
  println("Loading OCRE data...")
  XML.loadFile(fName)
}

/*

val header = "id#label#denomination#material#authority#mint#region\n"
*/
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








/*


import java.io.PrintWriter
new PrintWriter("ocre-types.cex"){write(header + data.mkString("\n")); close;}

*/
