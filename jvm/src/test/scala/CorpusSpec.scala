package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec



class CorpusSpec extends FlatSpec {

  "An Ocre" should "build an OHCO2 corpus for its legends" in {
    val ocre = OcreSource.fromFile("cex/ocre-cite-ids.cex")
    val corpus = ocre.corpus
    println("From OCRE with " + ocre.size + " issues, made corpus with " + corpus.size + " nodes.")
  }
}
