package edu.holycross.shot.nomisma

import org.scalatest.FlatSpec


class UrlManagerSpec extends FlatSpec {

  "The UrlManager object"  should "pretty print ID values" in {
    assert (UrlManager.prettyId("athens") == "Athens")
    assert (UrlManager.prettyId("aetolian_league") == "Aetolian League")
  }
  it should "generate ID component for URNs from OCRE URLs" in {
    val urlString = "5.gall(2).504"
    val expected = "5.gall_2.504"
    assert(UrlManager.ricIdFromUrl(urlString) == expected)
  }

}
