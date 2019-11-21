// create a Vector of BasicIssue s from CEX source

import scala.io.Source
import edu.holycross.shot.nomisma._

val f = "shared/src/test/resources/cex/ocre-basic-issues.cex"

val basics = for (cex <- Source.fromFile(f).getLines) yield {
    BasicIssue(cex)
}

val basicIssues = basics.toVector


val legendFile = "shared/src/test/resources/cex/ocre-legends.cex"
val legendList = for (cex <- Source.fromFile(legendFile).getLines) yield {
    Legend(cex)
}
val legends = legendList.toVector.flatten


val typesFile = "shared/src/test/resources/cex/ocre-types.cex"
val typeList = for (cex <- Source.fromFile(typesFile).getLines) yield {
    TypeDescription(cex)
}
val typeVector = typeList.toVector.flatten



val geoFile = "shared/src/test/resources/cex/mintgeo.cex"
val geoLines = Source.fromFile(geoFile).getLines.toVector.tail

val mintPoints = geoLines.map(MintPoint(_)).toVector

val ocre = Ocre(basicIssues, legends, typeVector, Vector.empty[Portrait], MintPointCollection(mintPoints))
