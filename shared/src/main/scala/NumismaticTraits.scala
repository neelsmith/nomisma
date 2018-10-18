package edu.holycross.shot.nomisma

sealed trait CoinSide
case object Obverse extends CoinSide
case object Reverse extends CoinSide


import java.net.URL
trait NomismaEntity {
  def url:  URL
  def label: String
}
