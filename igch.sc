import edu.holycross.shot.nomisma._

val igch = HoardSource.fromFile("jvm/src/test/resources/igch.rdf")

 val athColl = HoardCollection(igch.hoards.filter(_.mints.contains("athens")))




val hoardSets = Map(
"1.early" -> athColl.trimToAvgDateRange(-650,-510),
"2.lateArch" -> athColl.trimToAvgDateRange(-509,-480),
"3.classical1" -> athColl.trimToAvgDateRange(-479,-455),
"4.classical2" -> athColl.trimToAvgDateRange(-454,-432),
"5.classical3" -> athColl.trimToAvgDateRange(-431,-404),
"6.classical4"-> athColl.trimToAvgDateRange(-403,-323),
"7.hellen1" -> athColl.trimToAvgDateRange(-322,-280),
"8.hellen2" -> athColl.trimToAvgDateRange(-279,-89),
"9.hellen3" -> athColl.trimToAvgDateRange(-88,-1)
)

import java.io.PrintWriter
for ((hoard,coll) <- hoardSets) {
  println(hoard + ": " + coll.size + " hoards")
  val cg = HoardSource.contentsGraph(HoardCollection(coll))

  val mintLinks = cg.toKml
  val hoardColl = HoardCollection(coll)
  val hoardGeo = hoardColl.toKml

  new PrintWriter(hoard + "-mintLinks.kml") { write(mintLinks); close}
  new PrintWriter(hoard + "-hoards.kml") { write(hoardGeo); close}
}



//
