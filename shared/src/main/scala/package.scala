package edu.holycross.shot

package object nomisma {


  def idFromUrl (lod: String) : String = {
    lod.replaceAll(".+/","").replaceAll("#.+","")
  }

  def urlFromId (id: String) : String = {
    "http://nomisma.org/id/" + id
  }

  def prettyId(id: String) : String = {
    id.replaceAll("_"," ").split(' ').map(_.capitalize).mkString(" ")    
  }

}
