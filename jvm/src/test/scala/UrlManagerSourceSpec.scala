package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec


class UrlManagerSourceSpec extends FlatSpec {

  "The UrlManagerSource object" should "build a UrlManager from a File" in {
    val fName = "cex/ocre-url-urn-map.tsv"
    val urlMgr = UrlManagerSource.fromFile(fName, "\t")
    println("SIZE: " + urlMgr.size)
  }
  it should "build a UrlManager from a URL" in {
    val url = "https://raw.githubusercontent.com/neelsmith/nomisma/master/cex/ocre-url-urn-map.tsv"
    val urlMgr = UrlManagerSource.fromUrl(url, "\t")
    println("SIZE: " + urlMgr.size)
  }

}
