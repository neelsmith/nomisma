package edu.holycross.shot.nomisma
import java.net.URL

/**  Legend for a single side of a coin.
*
* @param coin
* @param side
* @param legend
*/
case class Portrait (coin: String, side: CoinSide, portrait: URL) extends NomismaEntity {

  def url = portrait
  def label = portrait.toString.replaceAll("http://collection.britishmuseum.org/id/person-institution/", "").replaceAll("http://nomisma.org/id/", "")

}


object Portrait {

  /** Given a Vector of OCRE Description nodes,
  * return a Vector of [[Portrait]]s.
  *
  * @param descriptionV Vector of OCRE Description nodes.
  */
  def portraitVector(descriptionV: Vector[scala.xml.Node]) : Vector[Portrait] = {
    val portraits = for (p <- descriptionV) yield {
      val rdgs = (p \\ "hasPortrait").toVector
      // p.attributes.value.toString is BOTH
      // coin ID and side
      if (rdgs.nonEmpty) {
        val coinParts = p.attributes.value.toString.split("#")

        ///println("ATTRS " + rdgs(0).attributes.toVector(0).value)

        val portraitUrl = new URL(rdgs(0).attributes.toVector(0).value.toString)

        val side = coinSide(coinParts(1))
        side match {
          case None => None
          case _  =>   Some(Portrait(
            coinParts(0),
            side.get,
            portraitUrl ))
        }

      //value ))

      } else {
        None
      }
    }
    portraits.flatten
  /*
    val portraitsRaw = for (p <- portraitText.flatten) yield {
      val triple = p.split("#")
      if (triple.size == 3) {
        Some(Portrait(triple(0), triple(1), triple(2))  )
      } else {
        println("Struck out on " + p)
        None
      }
    }
    portraitsRaw.filter(_.isDefined).map(_.get)
    */
  }
}
