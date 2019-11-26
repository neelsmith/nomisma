package edu.holycross.shot.nomisma

import edu.holycross.shot.cite._

sealed trait CoinSide
case object Obverse extends CoinSide
case object Reverse extends CoinSide


// import java.net.URL
trait NomismaEntity {
  def urlString:  String//URL
  def label: String
  def urn: Cite2Urn
}
