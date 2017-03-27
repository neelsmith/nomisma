package edu.holycross.shot

/** A library for working with freely available numismatic
* data from `nomisma.org`.
*
* ==Overview==
* The two fundamental classes are the [[edu.holycross.shot.nomisma.Hoard]] and the `Issue` (not yet implemented). Collections of Hoards (a [[HoardCollection]]) can
* be constructed from RDF XML data in a local file, or (not
* yet impemented) directly from the `nomisma.org` site.
*
* The package object includes utilities for fomatting and working
* strings used as LOD identifiers.
*
*/
package object nomisma {


  /** Extract the unique ID value from a long
  * identifying URL.
  *
  * @param lod Linked Open Data URL.
  */
  def idFromUrl (lod: String) : String = {
    lod.replaceAll(".+/","").replaceAll("#.+","")
  }

  /** Create `nomisma.org` URL from an ID value.
  *
  * @param id ID value.
  */
  def urlFromId (id: String) : String = {
    "http://nomisma.org/id/" + id
  }

  /** Pretty print ID value.
  *
  * @param id ID value.
  */
  def prettyId(id: String) : String = {
    id.replaceAll("_"," ").split(' ').map(_.capitalize).mkString(" ")
  }

}
