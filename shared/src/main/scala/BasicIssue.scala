package edu.holycross.shot.nomisma
import java.net.URL
import scala.scalajs.js
import js.annotation.JSExport


/**
* @param id
* @param labelText
* @param denomination
* @param material
* @param authority
* @param mint
* @param region
*/
@JSExport  case class BasicIssue(
  id: String,
  labelText:  String,
  denomination: String,
  material: String,
  authority: String,
  mint: String,
  region: String) extends NomismaEntity {

  def url = {
    new URL("http:nomisma.org/id/" + id)
  }
  def label = labelText
}


object BasicIssue {

  /** Create [[BasicIssue]]s from parsed XML of OCRE
  * RDF data.
  *
  * @param ocre Root of parsed OCRE RDF.
  */
  def parseOcreXml(ocre: scala.xml.Elem) : Vector[BasicIssue] = {
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

}
