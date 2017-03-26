package edu.holycross.shot.nomisma


// how igch models a closing date
case class ClosingDate (d1: Integer, d2: Option[Integer] = None) {
  val rangeOK = {
    d2 match {
      case None =>  true
      case _ => (d2.get > d1)
    }
  }
  require(rangeOK,s"Date range must be expressed in order earlier to later, not ${d1} to ${d2.get}")

  def pointAverage: Integer = {
    d2 match {
      case None => d1
      case i: Some[Integer] => (d1 + i.get) / 2
    }
  }


  override def toString = {
    d2 match {
      case d: Some[Integer] => s"${d1}-${d.get}"
      case _ => d1.toString
    }
  }


}


object ClosingDate {
  def apply(d1: Integer, d2: Integer): ClosingDate = {
    ClosingDate(d1, Some(d2))
  }
}
