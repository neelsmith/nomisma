package edu.holycross.shot.nomisma

//import com.esri.core.geometry._
import scala.scalajs.js
import js.annotation.JSExport

@JSExport case class Point(x: Double, y: Double) {

  override def toString = {
    x.toString + "," + y.toString
  }
}
