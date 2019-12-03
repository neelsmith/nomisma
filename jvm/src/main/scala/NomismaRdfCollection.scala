package edu.holycross.shot.nomisma

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

import scala.scalajs.js.annotation._

/** The contents of an edition of OCRE.
*
* @param issues
* @param legends
* @param typeDescriptions
* @param portraits
*/
@JSExportTopLevel("NomismaRdfCollection")
case class NomismaRdfCollection(
  issues:  Vector[BasicIssue],
  legends: Vector[Legend],
  typeDescriptions: Vector[TypeDescription],
  portraits: Vector[Portrait],
  dateRanges: Vector[IssueYearRange],
  mintsGeo: MintPointCollection
) extends LogSupport {

  Logger.setDefaultLogLevel(LogLevel.INFO)

  def toOcre(msgPoint: Int = 100): Ocre = {
    Ocre(ocreIssues(msgPoint))
  }

  /**  Construct a Vector of concrete [[OcreIssue]] from
  * the generic data of this trait. */
  def ocreIssues(msgPoint: Int) : Vector[OcreIssue] = {
    val ids = issues.map(_.id)
    ocreIssues(ids, msgPoint)
  }

  /**  Construct a Vector of concrete [[OcreIssue]] from
  * the generic data of this trait. */
  def ocreIssues(ids: Vector[String], msgPoint: Int = 100): Vector[OcreIssue] = {
    val total = ids.size
    val opts = for ((id,count) <- ids.zipWithIndex) yield {
      if (count % msgPoint == 0) {
        info(s"Converting ${id}: ${count + 1}/${total}")
      }
      ocreIssue(id)
    }
    //val opts = ids.map(ocreIssue(_))
    opts.flatten
  }

  /**  Construct a concrete [[OcreIssue]] for a given ID from
  * the generic data of this trait. */
  def ocreIssue(id: String) : Option[OcreIssue] = {
    val issueMatches = issues.filter(_.id == id)
     issueMatches.size  match {
      case 1 => {
        val basics = issueMatches(0)
        val geo = mintsGeo.forMint(basics.mint)

        val rTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Reverse)
        val rType = if (rTypeMatches.size == 1) {
          rTypeMatches(0).description
        } else {""}

        val oTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Obverse)
        val oType = if (oTypeMatches.size == 1) {
          oTypeMatches(0).description
        } else {""}

        val rLegendMatches = legends.filter(_.coin == id).filter(_.side == Reverse)
        val rLegend = if (rLegendMatches.size == 1) {
          rLegendMatches(0).legend
        } else {""}

        val oLegendMatches = legends.filter(_.coin == id).filter(_.side == Obverse)
        val oLegend = if (oLegendMatches.size == 1) {
          oLegendMatches(0).legend
        } else {""}

        val oPortaitMatches = portraits.filter(_.coin == id).filter(_.side == Obverse)
        val oPortrait = if (oPortaitMatches.size == 1) {
          oPortaitMatches(0).portrait
        } else {""}

        val rPortaitMatches = portraits.filter(_.coin == id).filter(_.side == Reverse)
        val rPortrait = if (rPortaitMatches.size == 1) {
          rPortaitMatches(0).portrait
        } else {""}


        val dateMatches = dateRanges.filter(_.coin == id)
        val dateRangeOpt = if (dateMatches.size == 1) {
          Some(dateMatches(0).yearRange)
        } else {None}


        Some(OcreIssue(basics.id, basics.labelText, basics.denomination, basics.material, basics.authority, basics.mint, basics.region, oType, oLegend, oPortrait, rType, rLegend,  rPortrait, dateRangeOpt))
      }
      case _ => None
    }
  }


// STUFF FOR CRRO CLASSES ///////////////////////////////
  def toCrro(msgPoint: Int = 100): Crro = {
    Crro(crroIssues(msgPoint))
  }

  /**  Construct a Vector of concrete [[CrroIssue]]s from
  * the generic data of this trait. */
  def crroIssues(msgPoint: Int) : Vector[CrroIssue] = {
    val ids = issues.map(_.id)
    crroIssues(ids, msgPoint)
  }

  /**  Construct a Vector of concrete [[CrroIssue]]s from
  * the generic data of this trait. */
  def crroIssues(ids: Vector[String], msgPoint: Int = 100): Vector[CrroIssue] = {
    val total = ids.size
    val opts = for ((id,count) <- ids.zipWithIndex) yield {
      if (count % msgPoint == 0) {
        info(s"Converting ${id}: ${count + 1}/${total}")
      }
      crroIssue(id)
    }
    //val opts = ids.map(ocreIssue(_))
    opts.flatten
  }

  /**  Construct a concrete [[CrroIssue]] for a given ID from
  * the generic data of this trait. */
  def crroIssue(id: String) : Option[CrroIssue] = {
    val issueMatches = issues.filter(_.id == id)
     issueMatches.size  match {
      case 1 => {
        val basics = issueMatches(0)
        val geo = mintsGeo.forMint(basics.mint)

        val rTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Reverse)
        val rType = if (rTypeMatches.size == 1) {
          rTypeMatches(0).description
        } else {""}

        val oTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Obverse)
        val oType = if (oTypeMatches.size == 1) {
          oTypeMatches(0).description
        } else {""}

        val rLegendMatches = legends.filter(_.coin == id).filter(_.side == Reverse)
        val rLegend = if (rLegendMatches.size == 1) {
          rLegendMatches(0).legend
        } else {""}

        val oLegendMatches = legends.filter(_.coin == id).filter(_.side == Obverse)
        val oLegend = if (oLegendMatches.size == 1) {
          oLegendMatches(0).legend
        } else {""}

        val oPortaitMatches = portraits.filter(_.coin == id).filter(_.side == Obverse)
        val oPortrait = if (oPortaitMatches.size == 1) {
          oPortaitMatches(0).portrait
        } else {""}

        val rPortaitMatches = portraits.filter(_.coin == id).filter(_.side == Reverse)
        val rPortrait = if (rPortaitMatches.size == 1) {
          rPortaitMatches(0).portrait
        } else {""}


        val dateMatches = dateRanges.filter(_.coin == id)
        val dateRangeOpt = if (dateMatches.size == 1) {
          Some(dateMatches(0).yearRange)
        } else {None}


        Some(CrroIssue(basics.id, basics.labelText, basics.denomination, basics.material, basics.authority, basics.mint, basics.region, oType, oLegend, oPortrait, rType, rLegend,  rPortrait, dateRangeOpt))
      }
      case _ => None
    }
  }

// STUFF FOR GENERIC CLASSES ///////////////////////////////

/**/
  def toGenericCatalog(msgPoint: Int = 100): GenericIssuesCatalog = {
    GenericIssuesCatalog(genericIssues(msgPoint))
  }

  /**  Construct a Vector of concrete [[GenericIssue]]s from
  * the generic data of this trait.
  */
  def genericIssues(msgPoint: Int) : Vector[GenericIssue] = {
    val ids = issues.map(_.id)
    genericIssues(ids, msgPoint)
  }

  def genericIssues(ids: Vector[String], msgPoint: Int = 100): Vector[GenericIssue] = {
    //val opts = ids.map(nomismaRdfIssue(_))
    val total = ids.size
    val opts = for ((id,count) <- ids.zipWithIndex) yield {
      if (count % msgPoint == 0) {
        info(s"Converting ${id}: ${count + 1}/${total}")
      }
      genericIssue(id)
    }
    opts.flatten
  }

  /** Given an object identifier, construct an [[genericIssue]].
  *
  * @param id Object identifier for issue.
  */
  def genericIssue(id: String) : Option[GenericIssue] = {

     val issueMatches = issues.filter(_.id == id)
      issueMatches.size  match {
       case 1 => {
         val basics = issueMatches(0)
         val geo = mintsGeo.forMint(basics.mint)

         val rTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Reverse)
         val rType = if (rTypeMatches.size == 1) {
           rTypeMatches(0).description
         } else {""}

         val oTypeMatches = typeDescriptions.filter(_.coin == id).filter(_.side == Obverse)
         val oType = if (oTypeMatches.size == 1) {
           oTypeMatches(0).description
         } else {""}

         val rLegendMatches = legends.filter(_.coin == id).filter(_.side == Reverse)
         val rLegend = if (rLegendMatches.size == 1) {
           rLegendMatches(0).legend
         } else {""}

         val oLegendMatches = legends.filter(_.coin == id).filter(_.side == Obverse)
         val oLegend = if (oLegendMatches.size == 1) {
           oLegendMatches(0).legend
         } else {""}

         val oPortaitMatches = portraits.filter(_.coin == id).filter(_.side == Obverse)
         val oPortrait = if (oPortaitMatches.size == 1) {
           oPortaitMatches(0).portrait
         } else {""}

         val rPortaitMatches = portraits.filter(_.coin == id).filter(_.side == Reverse)
         val rPortrait = if (rPortaitMatches.size == 1) {
           rPortaitMatches(0).portrait
         } else {""}


         val dateMatches = dateRanges.filter(_.coin == id)
         val dateRangeOpt = if (dateMatches.size == 1) {
           Some(dateMatches(0).yearRange)
         } else {None}


         Some(GenericIssue(basics.id, basics.labelText, basics.denomination, basics.material, basics.authority, basics.mint, basics.region, oType, oLegend, oPortrait, rType, rLegend,  rPortrait, dateRangeOpt))
       }
       case _ => None
     }
  }


  def kml : String = {
    mintsGeo.forMints(issues.map(_.mint)).mintPoints.map(_.toKml).mkString("\n\n")
  }
}


object NomismaRdfCollection {


  def addGeo(nomismaRdf: NomismaRdfCollection, geo: MintPointCollection): NomismaRdfCollection = {
    NomismaRdfCollection(nomismaRdf.issues, nomismaRdf.legends, nomismaRdf.typeDescriptions, nomismaRdf.portraits, nomismaRdf.dateRanges, geo)
  }
  /**
  *
  * @param nomismaRdf Root of parsed OCRE data set.
  */
  def parseRdf(root: scala.xml.Elem): NomismaRdfCollection = {

    // Enforce uniqe entries.
    val issues = BasicIssue.parseOcreXml(root)

    val descrs = root \\ "Description"
    val dv = descrs.toVector
    val legends = Legend.legendVector(dv)

    val typeDescrNodes = dv.filter( d => (d  \\ "description").size > 0 )
    val typeDescriptions =  TypeDescription.typeDescriptionVector(typeDescrNodes)

    val portraitElems = dv.filter( d => (d  \\ "hasPortrait").size > 0 )
    val portraits = Portrait.portraitVector(portraitElems)


    val typeData = root \\ "TypeSeriesItem"
    val dateRanges = IssueYearRange.datesVector(typeData.toVector)

    NomismaRdfCollection(issues, legends, typeDescriptions, portraits, dateRanges, MintPointCollection(Vector.empty[MintPoint]))

  }

}
