package edu.holycross.shot.nomisma


/**  Description of a coin's type on a single side.
*
* @param coin
* @param side
* @param description
*/
case class TypeDescription (coin: String, side: CoinSide, description: String)

object TypeDescription {


  /** Given a Vector of OCRE Description nodes,
  * return a Vector of [[TypeDescription]]s.
  *
  * @param descriptionV Vector of OCRE Description nodes.
  */
  def typeDescriptionVector(typeDescrs: Vector[scala.xml.Node]) : Vector[TypeDescription] = {
    val descriptionText = for (d <-typeDescrs) yield {
      val rdgs = d \\ "description"
      val rdg = rdgs(0)
      d.attributes.value.toString + "#" + rdg.text
    }
    val descrsRaw = for (d <- descriptionText) yield {
      val triple = d.split("#")
      if (triple.size == 3) {
      val side = coinSide (triple(1))
      side match {
        case None => {println("Bad formatting for coin side: " + triple(1)); None}
        case _  =>   Some(TypeDescription(triple(0),  side.get, triple(2))  )
      }


      } else {
        println("Struck out on description for " + d)
        None
      }
    }
    descrsRaw.filter(_.isDefined).map(_.get)
  }
}
