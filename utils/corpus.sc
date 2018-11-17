/*
Create an OCHO2 Corpus from an OCRE RDF file.
The Corpus will consist of all legends, cited by CtsUrns reflecting their nomisma ID.
*/
import edu.holycross.shot.nomisma._
import scala.xml._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

def textUrn(ocreId: String, side: CoinSide) : CtsUrn = {
  val urlBase = "http://numismatics.org/ocre/id/"
  val urnBase = "urn:cts:nomisma:ocre.v1:"
  val urnString = ocreId.replaceAll(urlBase,urnBase).replaceAll("ric.", "ric_").replaceAll("[\\(\\)]", "_")
  side match {
    case Obverse => CtsUrn(urnString + ".obv")
    case Reverse => CtsUrn(urnString + ".rev")
  }

}


def ocre(rdf: String) : Ocre = {
  val root = XML.loadFile(rdf)
  println("Parsing XML into Ocre\nPlease be patient....")
  Ocre.parseRdf(root)
}


def legendsToCorpus(legg : Vector[Legend]) : Corpus = {
  val nodes = legg.map( legend => CitableNode( textUrn(legend.coin, legend.side) , legend.legend) )
  Corpus(nodes)
}


def corpus(rdf: String) : Corpus = {
  val ocreRecords = ocre(rdf)
  legendsToCorpus(ocreRecords.legends)
}

println("Create an OCHO2 Corpus from an OCRE RDF file:")
println("\n\tcorpus(FILENAME)")
