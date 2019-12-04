import scala.xml._
import java.io.PrintWriter
import edu.holycross.shot.nomisma._

// OCRE is enormous.  Start sbt with lot of memory, e.g.,
// SBT_OPTS="-Xms512M -Xmx4096M -XX:MaxMetaspaceSize=1024M" sbt

// What this script can do:
// 1.  read OCRE RDF
// 2.  Extract for each issue obverse type, obverse legend, obverse portrait, reverse type, reverse legend, reverse portrait
// 3.  Join these issue records to a previously extracted set of records with basic data about the issue

val currentRdf = "rdf/ocre-2019-11-28.rdf"
/** Load OCRE XML from a named file.
*
* @param fName Name of file to load.
*/
def loadOcre(fName: String = currentRdf): scala.xml.Elem = {
  println("Loading OCRE data...")
  XML.loadFile(fName)
}

def summarizeRdf(ocreRdf: NomismaRdfCollection) : Unit = {
    println("Expected issues from nosmiam.org web: 50698")

    println("\n\nIssues:  " + ocreRdf.issues.size)
    println("Date ranges: " + ocreRdf.dateRanges.size)
    println("Type descriptions: " + ocreRdf.typeDescriptions.size)
    println("Legends: " + ocreRdf.legends.size)
    println("Portrait IDs: " + ocreRdf.portraits.size)
}

def loadAndParse(fName: String = currentRdf): NomismaRdfCollection = {
  val root = loadOcre(fName)
  NomismaRdfCollection.parseRdf(root)
}


def info = {
  println("Things you can do with this script:\n")

  println("\tloadOcre(fName: String): scala.xml.Elem")
  println("\tloadAndParse(fName: String): NomismaRdfCollection")
  println("\tsummarizeRdf(ocre: NomismaRdfCollection) : Unit")

  println("\nTo see this message:\n\n\tinfo")
}



println("\n\n ")
info


val rdf = loadAndParse()
println("Total RDF issues: " + rdf.issues.size)


println("\nCHECK BASIC ISSUES FOR CONSISTENCY:")
println("Unique issues: " + rdf.issues.distinct.size)
val dupeCount = rdf.issues.size - rdf.issues.distinct.size
println("Difference: " + dupeCount)

val dupes = rdf.issues.map(_.id).groupBy( i => i).filter(_._2.size > 1)
println("Duplicate issues: " + dupes.keySet.size)
if (dupeCount == dupes.size) {
  println("Each duplicate appears exactly twice.")
}

println("\nCHECK DATE RANGES:")
println("Date range records: " + rdf.dateRanges.size)
println("Distinct date range records: " + rdf.dateRanges.distinct.size)
require(rdf.dateRanges.distinct.size <= rdf.issues.size)




println("\nCHECK TYPE DESCRIPTIONS:")
val typesBySide = rdf.typeDescriptions.groupBy(_.side)
val obvTypes = typesBySide(Obverse)
println("Obv types: " + obvTypes.size)
println("Distinct obv types: " + obvTypes.distinct.size)

val dupeOTypes = obvTypes.map(_.coin).groupBy( i => i).filter(_._2.size > 1)


val revTypes = typesBySide(Reverse)
println("Rev types: " + revTypes.size)
println("Distinct rev types: " + revTypes.distinct.size)
