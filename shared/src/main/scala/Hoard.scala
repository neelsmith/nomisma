package edu.holycross.shot.nomisma

case class Hoard (
  id: String,
  label: String,
  closingDate: Option[ClosingDate],
  mints: Vector[String],
  geo: Option[String]
) {

  val dateLabel: String = {
    closingDate match {
      case None => "No date given"
      case _ => "Date: " + closingDate.get
    }
  }

  def prettyPrint = {
    println(s"${label} (${id})")
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
