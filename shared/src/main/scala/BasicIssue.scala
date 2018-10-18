package edu.holycross.shot.nomisma
import java.net.URL
import scala.scalajs.js
import js.annotation.JSExport

@JSExport  case class BasicIssue(id: String, labelText:  String,
denomination: String, material: String, authority: String, mint: String, region: String) extends NomismaEntity {

  def url = {
    new URL("http:nomisma.org/id/" + id)
  }
  def label = labelText
}
