package edu.holycross.shot

package object nomisma {


  def idFromUrl (lod: String) : String = {
    lod.replaceAll(".+/","").replaceAll("#.+","")
  }

  def urlFromId (id: String) : String = {
    "http://nomisma.org/id/" + id
  }

}
