import edu.holycross.shot.nomisma._

val igch = HoardSource.fromFile("jvm/src/test/resources/igch.rdf")

 val athColl = HoardCollection(igch.hoards.filter(_.mints.contains("athens")))
 val cg = HoardSource.contentsGraph(athColl)
 val kml = cg.toKml

// new PrintWriter("filename") { write("file contents"); close }
