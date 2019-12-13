package edu.holycross.shot.nomisma

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import org.scalatest.FlatSpec


class TokenizingSpec extends FlatSpec {

  "The DiplomaticOcreOrthography object" should "tokenize text nodes" in {
    val urn = CtsUrn("urn:cts:hcnum:issues.ric.raw:1_2.aug.530.obv")
    val txt = "AVGVS TR POT|AVGVS TR PO"
    val cn = CitableNode(urn,txt)

    val tokens = DiplomaticOcreOrthography.tokenizeNode(cn)
    //println(tokens.mkString("\n"))
  }

  it should "recognize valid diplomatic orthography" in {
    assert(DiplomaticOcreOrthography.validString("senatvs consvlto") == false)
    assert(DiplomaticOcreOrthography.validString("SENATVS CONSVLTO") )

  }

  "The NormalizedOcreOrthography object" should "tokenize text nodes" in {
    val urn = CtsUrn("urn:cts:hcnum:issues.ric.raw:1_2.vit.158.rev")
    val txt = "senatvs consvlto"
    val cn = CitableNode(urn,txt)

    val tokens = NormalizedOcreOrthography.tokenizeNode(cn)
    println("TOKENS: " + tokens.size)
    println(tokens.mkString("\n"))
  }

  it should "recognize valid othography" in {
    assert(NormalizedOcreOrthography.validString("senatvs consvlto"))
    assert(NormalizedOcreOrthography.validString("SENATVS consvlto") == false)

  }
}
