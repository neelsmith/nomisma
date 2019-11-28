package edu.holycross.shot.nomisma


import scala.scalajs.js.annotation._
/**  Description of a coin's type on a single side.
*
* @param coin
* @param side
* @param description
*/
@JSExportTopLevel("TypeDescription")
case class TypeDescription (coin: String, side: CoinSide, description: String)

object TypeDescription {



  def apply(cex: String): Vector[TypeDescription] = {
    val cols = cex.split("#")
    if (cols.size < 3) {
      Vector.empty[TypeDescription]
    } else {
      val otype = TypeDescription(cols(0), Obverse, cols(1))
      val rtype = TypeDescription(cols(0), Reverse, cols(2))
      Vector(otype, rtype)
    }
  }


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
      val id = ricIdFromUrl(triple(0))

      side match {
        case None => {println("Bad formatting for coin side: " + triple(1)); None}
        case _  =>   Some(TypeDescription(id,  side.get, triple(2))  )
      }


      } else {
        println("Failed to parse type description from " + d)
        None
      }
    }
    descrsRaw.filter(_.isDefined).map(_.get)
  }
}
