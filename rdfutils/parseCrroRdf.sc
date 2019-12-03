import scala.xml._
import java.io.PrintWriter
import edu.holycross.shot.nomisma._

val currentRdf = "rdf/crro.rdf"
val outputFile = "crro-2019-12-03.cex"
/** Load OCRE XML from a named file.
*
* @param fName Name of file to load.
*/
def loadRdf(fName: String = currentRdf): scala.xml.Elem = {
  println("Loading Rrco data...")
  XML.loadFile(fName)
}

def summarizeRdf(rdf: NomismaRdfCollection) : Unit = {
  println("\n\nIssues:  " + rdf.issues.size)
  println("Date ranges: " + rdf.dateRanges.size)
  println("Type descriptions: " + rdf.typeDescriptions.size)
  println("Legends: " + rdf.legends.size)
  println("Portrait IDs: " + rdf.portraits.size)
}

def summarizeCrro(crro: NomismaRdfCollection) : Unit = {
  println("\n\nIssues:  " + crro.issues.size)

  println("Date ranges: " + crro.dateRanges.size)
  println("Type descriptions: " + crro.typeDescriptions.size)
  println("Legends: " + crro.legends.size)
  println("Portrait IDs: " + crro.portraits.size)

}

def loadAndParse(fName: String = currentRdf): NomismaRdfCollection = {
  val root = loadRdf(fName)
  NomismaRdfCollection.parseRdf(root)
}

def crro(fName: String = currentRdf): Crro = {
  val rdf = loadAndParse(fName)
  println("Converting NomismaRdfCollection to Rcco...")
  val o = rdf.toCrro()
  println("Done.")
  o
}

def cex(src: String = currentRdf, outFile: String ) : Unit = {
  val o = crro(src)
  println("Writing CRRO corpus to " + outFile + "...")
  new PrintWriter(outFile){write(o.cex);close;}
}
def info = {
  println("Things you can do with this script:\n")

  println("\tloadRdf(fName: String): scala.xml.Elem")
  println("\tloadAndParse(fName: String): NomismaRdfCollection")
  println("\tsummarizeCrro(crro: Crro) : Unit")

  println("\n\tcrro(fName: String): Rcco")
  println("\tcex(src: String, outFile: String)")

  println("\nTo see this message:\n\n\tinfo")
}


println("\n\n ")
info
