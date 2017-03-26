package edu.holycross.shot.nomisma

case class Hoard (
  id: String,
  label: String,
  dateStr: Option[String],
  mints: Vector[String],
  geo: Option[String]
) {

  val dateLabel: String = {
    dateStr match {
      case None => "No date given"
      case _ => "Date: " + dateStr.get
    }
  }

  def prettyPrint = {
    println(id)
    println(dateLabel)
    geo match {
      case None => println("Location unknown")
      case _ => println("Location: " + geo.get)
    }
    println("Contains coins from mints:")
    for (m <- mints) {
      println("\t" + m.split(' ').map(_.capitalize).mkString(" "))
    }
  }


// lOD URL for mint!
/*
  def kmlPoint: String = {
    val mintsList = mints.mkString("\n")
    """
    <Placemark>
    <name>${id}</name>
    <description>${dateLabel}\n\n""" +
    raw."""${mintsList}</description>
    <Point>
      <coordinates>${geo.getOrElse("")},0</coordinates>
    </Point>
    </Placemark>
    """
  }
  */
}
