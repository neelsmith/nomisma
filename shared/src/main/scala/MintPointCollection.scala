package edu.holycross.shot.nomisma

import scala.scalajs.js
import js.annotation.JSExport


/** Wrapping class for a Vector of [[MintPoint]]s.
*
* @param mintPoints Vector of [[MintPoint]]s.
*/
@JSExport case class MintPointCollection(mintPoints: Vector[MintPoint] )  {


  /** Number of mints in the collection.
  */
  def size: Integer = {
    mintPoints.size
  }

  /** Geographic point for mint identifier.
  *
  * @param mintName String identifier for mint.
  */
  def forMint(mintName: String): Option[MintPoint] = {
    val mintUrl = "http://nomisma.org/id/" + mintName + ".rdf"
    val srch = mintPoints.filter(_.mint == mintUrl)
    if (srch.isEmpty) {
      None
    } else {
      Some(srch(0))
    }
  }


  /** Create a [[MintPointCollection]], given a Vector of mint identifiers.
  *
  * @param mints Vector of string values identifying mints.
  */
  def forMints(mints: Vector[String]): MintPointCollection = {
    val opts = mints.map(forMint(_))
    MintPointCollection(opts.flatten)
  }
}
