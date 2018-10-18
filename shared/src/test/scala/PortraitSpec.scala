package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec


class PortraitSpec extends FlatSpec {

  "A Portrait"  should "have a CoinSide" in {
    val portrait = Portrait("coinId", Obverse, "EX SC")
    assert (portrait.side == Obverse)
  }

}
