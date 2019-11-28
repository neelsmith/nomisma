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
def loadOcre(fName: String = "ocre-2019-11-28.rdf"): scala.xml.Elem = {
  println("Loading OCRE data...")
  XML.loadFile(fName)
}

def summary(ocre: OcreRdf) : Unit = {
    println("\n\nIssues:  " + ocre.issues.size)
    println("Date ranges: " + ocre.dateRanges.size)
    println("Type descriptions: " + ocre.typeDescriptions.size)
    println("Legends: " + ocre.legends.size)
    println("Portrait IDs: " + ocre.portraits.size)
}

def loadAndParse(fName: String = "ocre-2019-11-28.rdf"): OcreRdf = {
  val root = loadOcre(fName)
  OcreRdf.parseRdf(root)
}


def info = {
  println("Things you can do with this script:\n")

  println("\tloadOcre(fName: String): scala.xml.Elem")
  println("\tloadAndParse(fName: String): OcreRdf")
  println("\tsummary(ocre: OcreRdf) : Unit")
  println("\nTo see this message:\n\n\tinfo")
}


println("\n\n ")
info
