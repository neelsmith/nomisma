package edu.holycross.shot.nomisma


/**  Legend for a single side of a coin.
*
* @param coin
* @param side
* @param legend
*/
case class Legend (coin: String, side: CoinSide, legend: String)


object Legend {


  def apply(cex: String): Vector[Legend] = {
    val cols = cex.split("#")
    if (cols.size < 3) {
      Vector.empty[Legend]
    } else {
      val olegend = Legend(cols(0), Obverse, cols(1))
      val rlegend = Legend(cols(0), Reverse, cols(2))
      Vector(olegend, rlegend)
    }
  }
  /** Given a Vector of RDF Description nodes for OCRE data,
  * return a Vector of [[Legend]]s.
  *
  * @param descriptionV Vector of OCRE Description nodes.
  */
  def legendVector(descriptionV: Vector[scala.xml.Node]) : Vector[Legend] = {
    val legendsElems = descriptionV.filter( d => (d  \\ "hasLegend").size > 0 )
    val legendsText = for (l <-legendsElems) yield {
      val rdgs = l \\ "hasLegend"
      val rdg = rdgs(0)
      l.attributes.value.toString + "#" + rdg.text
    }
    val legendsRaw = for (l <- legendsText) yield {
      val triple = l.split("#")
      if (triple.size == 3) {
        val side = coinSide (triple(1))
        side match {
          case None => None
          case _  => Some(Legend(triple(0),  side.get, triple(2) ))

        }

      } else {
        println("Struck out on " + l)
        None
      }
    }
     legendsRaw.filter(_.isDefined).map(_.get)
  }
}
