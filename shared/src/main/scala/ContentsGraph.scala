package edu.holycross.shot.nomisma

//import com.esri.core.geometry._
import scala.scalajs.js
import js.annotation.JSExport

@JSExport case class MintPoint(mint: String, pt: Point)

/**
*/
@JSExport case class ContentsGraph (
  id: String,
  hoard: Point,
  mintPoints: Vector[MintPoint]
)
