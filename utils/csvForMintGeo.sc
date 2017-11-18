/*
Q&D script to read all RDF files in `ids` directory, look for `SpatialThing`
content, and write out a simple CSV file for all mints with geographic locations
to "mintpoints.csv"
*/
import scala.io.Source
import scala.xml._
import java.io.File

val d = new File("ids")
val rdfFiles = d.listFiles.filter(_.isFile).toVector


def idFromUrl (lod: String) : String = {
  lod.replaceAll(".+/","").replaceAll("#.+","")
}


def spatialForNode(n: scala.xml.Node) = {
  var hoardKey = ""
  val attV = n.attributes.toVector
  for (a <- attV) {
    if (a.key == "about") {
      hoardKey = idFromUrl( a.value.text)
    } else {}
  }
  val lat = n \ "lat"
  val lon = n \ "long"
  s"${hoardKey},${lon.text.toDouble},${lat.text.toDouble}"
}



val coords = for (rdfFile <- rdfFiles) yield {
  val root = XML.loadFile(rdfFile)
  val spatialThings = root \\ "SpatialThing"
  if (spatialThings.size != 1) {
      ""
  } else {
    try {
      spatialForNode(spatialThings(0))

      //Some(MintPoint(spatial._1 , spatial._2))
    } catch {
      case e : Throwable => {
        println("Something went wrong: " + e)
        ""
      }
    }
  }
}

val hdr = "mint,lon,lat\n"
import java.io.PrintWriter
new PrintWriter("mintpoints.csv") {write (hdr + coords.filter(_.nonEmpty).mkString("\n")); close }
