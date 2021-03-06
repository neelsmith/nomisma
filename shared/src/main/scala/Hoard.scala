package edu.holycross.shot.nomisma



import scala.scalajs.js.annotation._

/** A coin hoard.
*
* @param id Unique identifier: last element of `nomisma.org` URL identifier.
* @param label Human-readable label; contents of `prefLabel` element
* in `nomisma.org` RDF XML.
* @param closingDate Closing date of the hoard.
* @param mints Mints represented in this hoard, identified by
* `nomisma.org` identifier.
* @param geo Location of hoard as a geographic point.
*/
@JSExportTopLevel("Hoard")
case class Hoard (
  id: String,
  label: String,
  closingDate: Option[YearRange],
  mints: Vector[String],
  geo: Option[Point]
) {


  def csv : String  = {

    val mintList = mints.mkString("#")
    s"${id},${label},${closingCsv},${mintList},${geoCsv}"
  }

  val closingCsv: String = {
    closingDate match {
      case None => ","
      case _ => closingDate.get.csv(",")
    }
  }
  def pointAverage: Option[Integer] = {
    closingDate match {
      case None => None
      case cd: Some[YearRange] => Some(cd.get.pointAverage)
    }
  }


  /** Formatted string for closing date.
  */
  def dateLabel: String = {
    closingDate match {
      case None => "No date given"
      case _ => "Date: " + closingDate.get
    }
  }


  def prettyPrint: String = {
    val idDateLabel = s"${label} (${id})\n"  + dateLabel

    val geoString = geo match {
      case None => "Location unknown"
      case _ => "Location: " + geo.get
    }
    val mintHeader = "Contains coins from mints:"
    val mintString = for (m <- mints) yield {
      "\t" + UrlManager.prettyId(m)
    }
    idDateLabel + geoString + mintHeader + mintString
  }

  def geoString : String = {
    geo match {
      case None => ""
      case ptOpt: Some[Point] => {
        val pt = ptOpt.get
        pt.x.toString +","+pt.y.toString
      }
    }
  }

  def geoCsv : String = {
      geo match {
        case None => ","
        case ptOpt: Some[Point] => {
          val pt = ptOpt.get
          pt.x.toString +","+pt.y.toString
        }
      }
  }

  def mintsHtml: String = {
    val wrapped = mints.map {
      s => "<li><a href='" + UrlManager.urlFromId(s) + "'>" + UrlManager.prettyId(s) + "</a></li>"
    }
    "<ul>" + wrapped.mkString("\n") + "</ul>"
  }
  /*
<<<<<<< HEAD


  def sizeUrl: String = {
    mints.size match {
      case n if (n >= 36) => "#group6"
      case n if (n >= 22 && n < 36) => "#group5"

      case _ => "#group1" */
      /*
      case n if (n >= 36) => "#group6"
      case n if (n >= 36) => "#group6"
      case n if (n >= 36) => "#group6"
      case n if (n >= 36) => "#group6"
      */
    //}
  //}
  /*
  36-66
  22-33
  7-19
  4-6
  2-3
  1
  */

  /*
  def kmlPoint: String = {
=======
>>>>>>> 3d7500cf10ba0aa4f0147491182cdd723b159a34
*/


  def sizeUrl: String = {
    mints.size match {
      case n if (n >= 36) => "#group6"
      case n if (n >= 22 && n < 36) => "#group5"
      case n if (n >= 7 && n < 22) => "#group4"
      case n if (n >= 4 && n < 7) => "#group3"
      case n if (n >= 2 && n < 4) => "#group2"
      case n if (n == 1) => "#group1"

    }
  }

  def mintUrl: String = {
    "<a href='http://coinhoards.org/id/" + id + "'>" + label + "</a>"
  }

  def kmlPoint: String = {
    geo match {
      case None => ""
      case _ =>
    raw"""
    <Placemark>
    <name>${id}</name>

    <description><p>${mintUrl}, ${dateLabel}</p>
    ${mintsHtml}</description>

    <styleUrl>${sizeUrl}</styleUrl>
    <Point>
      <coordinates>${geoString},0</coordinates>
    </Point>
    </Placemark>
    """
  }
  }
  def delimited(delimiter: String = "#"): String = {
    val dateDisplay = {
       closingDate match {
         case None => ""
         case _ => closingDate.get
       }
    }

    geo match {
      case None => ""
      case _ =>
        raw"""${id}${delimiter}${label}${delimiter}${dateDisplay}${delimiter}${geo.get.x}${delimiter}${geo.get.y}"""
    }
  }
}
