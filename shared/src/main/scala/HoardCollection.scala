package edu.holycross.shot.nomisma


import scala.xml._
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map

import scala.scalajs.js.annotation._

/** A collection of [[Hoard]]s.
*
* @param hoards Vector of [[Hoard]]s in this collection.
*/
@JSExportTopLevel("HoardCollection")
case class HoardCollection(hoards: Vector[Hoard])  {


  /*
  val preface = """<?xml version="1.0" encoding="UTF-8"?>
  <kml xmlns="http://www.opengis.net/kml/2.2">
    <Document>
    <Style id="group1">
      <IconStyle>
        <scale>0.5</scale>
        <Icon>
          <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group2">
      <IconStyle>
        <Icon>
          <scale>1.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group3">
      <IconStyle>
        <Icon>
          <scale>8.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group4">
      <IconStyle>
        <Icon>
          <scale>16.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group5">
      <IconStyle>
        <Icon>
          <scale>32.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group6">
      <IconStyle>
        <Icon>
          <scale>64.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
  """
*/
  /** Conclusion to KML document.
  */
  val trail = "</Document></kml>"


  /** Number of hoards in the collection.
  */
  def size: Int = {
    hoards.size
  }


  def hasMint : HoardCollection = {
    HoardCollection(hoards.filter(_.mints.nonEmpty))
  }
  /** Create a new HoardCollection containing only
  * hoards with known geographic location.
  */
  def located: HoardCollection = {
    HoardCollection(hoards.filter(_.geo !=  None))
  }

  def dated: HoardCollection = {
    HoardCollection(hoards.filter(_.closingDate !=  None))
  }


  def closingDateVector: Vector[YearRange] = {
    dated.hoards.map(_.closingDate.get)
  }

  /** Set of mints represented in this collection
  * of hoards.
  */
  def mintSet: Set[String] = {
    hoards.flatMap(_.mints).toSet
  }

  /** Maximum of all `pointAverage` values in the collection.
  */
  def maxAvgDate = {
    val temp = dated.hoards.map(_.pointAverage.get)
    temp.max
  }

  def maxDate = {
    val year1List = dated.hoards.map(_.closingDate.get.year1)
    //val d2List = dated.hoards.filter(_.closingDate.get.d2 != None).map(_.closingDate.get.d2)
    //if (year1List.max > d2List.max) { year1List.max} else {d2List.max}
    year1List.max

  }

  /** Minimum of all `pointAverage` values in the collection.
  */
  def minAvgDate = {
    val temp = dated.hoards.map(_.pointAverage.get)
    temp.min
  }

  def trimToAvgDateRange(year1: Integer, d2: Integer) = {
    dated.hoards.filter(_.pointAverage.get >= year1).filter(_.pointAverage.get <= d2)
  }

  def csv: String = {
    val hdr = "ID,label,date1,date2,mints,lon,lat\n"
    hdr + hoards.map(_.csv).mkString("\n")
  }

/*
  val maxDate: Integer = {
    val maxD1 = hoards.map(_.closingDate.year1).max
    val maxD2 = hoards.map(_.closingDate.d2).max
    if (maxD1 > maxD2) {
      maxD1
    } else {
      maxD2
    }
  }
*/
/*  def toKml: String = {
    preface + hoards.map(_.kmlPoint).mkString("\n") + trail
  }

<<<<<<< HEAD
*/
  /** Preface to KML document.
  */
  val preface = """<?xml version="1.0" encoding="UTF-8"?>
  <kml xmlns="http://www.opengis.net/kml/2.2">
    <Document>
    <Style id="group1">
      <IconStyle>
        <scale>0.5</scale>
        <Icon>
          <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group2">
      <IconStyle>
        <Icon>
          <scale>1.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group3">
      <IconStyle>
        <Icon>
          <scale>2.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group4">
      <IconStyle>
        <Icon>
          <scale>4.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group5">
      <IconStyle>
        <Icon>
          <scale>6.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
    <Style id="group6">
      <IconStyle>
        <Icon>
          <scale>8.0</scale>
            <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle_highlight.png</href>
        </Icon>
      </IconStyle>
    </Style>
  """
  /*
=======
  def delimitedText(separator: String = "#"): String = {
    val csvHeader = "id,label,date,lon,lat\n"
    csvHeader + hoards.map(_.delimited(separator)).mkString("\n")
  }
>>>>>>> 3d7500cf10ba0aa4f0147491182cdd723b159a34
*/
}
