package edu.holycross.shot.nomisma
import edu.holycross.shot.cite._
import java.net.URL
import scala.scalajs.js
import js.annotation.JSExport



@JSExport  case class OcreIssue(
    id: String,
    labelText:  String,
    denomination: String,
    material: String,
    authority: String,
    mint: String,
    region: String,
    obvType: String,
    obvLegend: String,
    obvPortraitId: String,
    revType: String,
    revLegend: String,
    revPortraitId: String,
    startDate: Option[Int],
    endDate: Option[Int]

  ) extends NomismaEntity {

  def url = {
    new URL("http:nomisma.org/id/" + id)
  }

  def urn: Cite2Urn = {
    Cite2Urn("urn:cite2:nomisma:ocre.hc:" + id)
  }
  def label = labelText

  def kml: String = ""
/*
  def kml: String = {
    mintGeo match {
      case None => ""
      case _ => mintGeo.get.toKml
    }
  }*/
}



object OcreIssue {


  def apply(cex: String) : OcreIssue = {
    def cols = cex.split("#")
    if (cols.size < 15) {
      throw new Exception("Could not parse CEX string for OcreIssue: too few columns in " + cex)
    }

    val startDate: Option[Int] = try {
      val i = cols(13).toInt
      Some(i)
    } catch {
      case t: Throwable => {
        println("Failed to parse " + cols(13) + " as Int (coin ID " + cols(0) + ")" )
        None
      }
    }

    val endDate: Option[Int] = try {
      val i = cols(14).toInt
      Some(i)
    } catch {
      case t: Throwable => {
        println("Failed to parse " + cols(14) + " as Int (coin ID " + cols(0) + ")" )
        None
      }
    }
    OcreIssue(cols(0), cols(1), cols(2), cols(3), cols(4), cols(5), cols(6).trim, cols(7).trim, cols(8).trim, cols(9).trim, cols(10).trim, cols(11).trim, cols(12).trim, startDate, endDate)
  }
}
