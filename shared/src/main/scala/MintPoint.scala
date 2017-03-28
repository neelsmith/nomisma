package edu.holycross.shot.nomisma

//import com.esri.core.geometry._
import scala.scalajs.js
import js.annotation.JSExport

@JSExport case class MintPoint(mint: String, pt: Point)

@JSExport case class MintPointCollection(mintPoints: Vector[MintPoint] )  {

  def size: Integer = {
    mintPoints.size
  }
  def forMint(mintName: String): Option[MintPoint] = {
    val srch = mintPoints.filter(_.mint == mintName)
    if (srch.isEmpty) {
      None
    } else {
      Some(srch(0))
    }
  }

  def forMints(mints: Vector[String]): MintPointCollection = {
    var rslt = scala.collection.mutable.ArrayBuffer[MintPoint]()
    for (m <- mints) {
      val pt = forMint(m)
      pt match  {
        case None =>
        case _ => rslt += pt.get
      }
    }
    MintPointCollection(rslt.toVector)
  }
}
