package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec


class TypeDescriptionSpec extends FlatSpec {

  "A TypeDescription"  should "have a CoinSide" in {
    val typeDescription = TypeDescription("coinId", Obverse, "Laureate portrait, r.")
    assert (typeDescription.side == Obverse)
  }

}
