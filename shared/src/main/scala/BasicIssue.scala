package edu.holycross.shot.nomisma
//import java.net.URL
import scala.scalajs.js
import scala.scalajs.js.annotation._



/**
* @param id
* @param labelText
* @param denomination
* @param material
* @param authority
* @param mint
* @param region
*/
@JSExportTopLevel("BasicIssue")
case class BasicIssue(
  id: String,
  labelText:  String,
  denomination: String,
  material: String,
  authority: String,
  mint: String,
  region: String) {

  def urlString = {
    //new URL("http:nomisma.org/id/" + id)
    id
  }
  def label = labelText
}


object BasicIssue {


  def apply(cex: String) : BasicIssue = {
    def cols = cex.split("#")
    if (cols.size < 7) {
      throw new Exception("Could not parse CEX string for BasicIssue: too few columns in " + cex)
    }
    BasicIssue(cols(0), cols(1), cols(2), cols(3), cols(4), cols(5), cols(6))
  }


  /** Create [[BasicIssue]]s from parsed XML of OCRE
  * RDF data.
  *
  * @param ocre Root of parsed OCRE RDF.
  */
  def parseOcreXml(ocre: scala.xml.Elem) : Vector[BasicIssue] = {
    val typeSeries = ocre \\ "TypeSeriesItem"
    val typesVect = typeSeries.toVector

    val data = for (t <- typesVect) yield {
      // apparently, all records DO have an id,
      // a label, and an authority,
      // so not adding onerous error checking
      //val id =  t.attributes.value(0).toString.
      val id = UrlManager.ricIdFromUrl(t.attributes.value(0).toString)

      val lab = (t \\ "prefLabel")(0).text

      val authorityElems = (t \\ "hasAuthority")
      val  authority =  if (authorityElems.isEmpty) { "none" } else {     authorityElems(0).attributes.value.toString.replaceFirst("http://nomisma.org/id/","")
      }

      // but some records have no denomination, material,
      // mint, or region, OR
      // have null string values for those (wtf??)
      val denomElems = (t \\ "hasDenomination")
      val  denom =   if (denomElems.isEmpty) {
        "none"
      } else {
         val attrs = denomElems(0).attributes
         if (attrs.isEmpty) {
            "none"
         } else {
          denomElems(0).attributes.value.toString.replaceFirst("http://nomisma.org/id/","")
         }
      }

      val materialElems = (t \\ "hasMaterial")
      val  material =  if (materialElems.isEmpty) {
        "none"
      } else {
        val attrs = materialElems(0).attributes
         if (attrs.isEmpty) {
           "none"
         } else {
          materialElems(0).attributes.value.toString.replaceFirst("http://nomisma.org/id/","")
        }
      }

      val mintElems = (t \\ "hasMint")
      val  mint =  if (mintElems.isEmpty) {
        "none"
      } else {
        val attrs = mintElems(0).attributes
         if (attrs.isEmpty) {
           "none"
         } else {
           mintElems(0).attributes.value.toString.replaceFirst("http://nomisma.org/id/","")
         }
      }

      val regionElems = (t \\ "hasRegion")
      val  region =  if (regionElems.isEmpty) {
        "none"
      } else {
        val attrs = regionElems(0).attributes
         if (attrs.isEmpty) {
           "none"
         } else {
          regionElems(0).attributes.value.toString.replaceFirst("http://nomisma.org/id/","")
        }
      }
      BasicIssue(id, lab, denom, material, authority, mint, region)
    }
    data.toVector
  }

}
