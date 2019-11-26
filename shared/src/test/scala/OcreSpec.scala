package edu.holycross.shot.nomisma
import org.scalatest.FlatSpec


class OcreSpec extends FlatSpec {

  val cex = """ID#Label#Denomination#Metal#Authority#Mint#Region#ObvType#ObvLegend#ObvPortraitId#RevType#RevLegend#RevPortraitId#StartDate#EndDate
1_2.aug.10#RIC I (second edition) Augustus 10#denarius#ar#augustus#emerita#lusitania#Head of Augustus, bare, right#IMP CAESAR AVGVSTV#http://nomisma.org/id/augustus#City wall, gateway#P CARISIVS LEG PRO PR EMERITA# #-0025#-0023# #
1_2.aug.100#RIC I (second edition) Augustus 100#denarius#ar#augustus#colonia_patricia#lusitania#Toga picta over tunica palmata between aquila, left, and wreath, right#S P Q R  PARENT CONS SVO#http://nomisma.org/id/augustus#Slow quadriga right, with four miniature horses#CAESARI AVGVSTO# #-0018#-0018# #
"""

  "An Ocre instance" should "have a size function" in {
    val ocre = Ocre(cex, true)
    val expectedSize = 2
    assert(ocre.size == expectedSize)
  }

  it should "extract an Ocre of dated issues" in {
    val ocre = Ocre(cex, true)
    val dated = ocre.datable
    val expectedSize = 2
    assert(ocre.size == expectedSize)
  }

  it should "find the lowest date in the Ocre" in {
    val ocre = Ocre(cex, true)
    val expectedMin : Int = -25
    assert(expectedMin == ocre.minDate)
  }

  it should "find the highest date in the Ocre" in {
    val ocre = Ocre(cex, true)
    val expectedMax : Int = -18
    assert(expectedMax == ocre.maxDate)
  }

  it should "find a date range for the Ocre" in {
    val ocre = Ocre(cex, true)
    val expectedRange = YearRange(-25, Some(-18))
    assert (expectedRange == ocre.dateRange)
  }

  it should "subset the Ocre by authority" in {
    val ocre = Ocre(cex, true)
    val authMap = ocre.byAuthority
    val expectedSize = 2
    assert(authMap("augustus").size == expectedSize)

    val expectedRange = YearRange(-25, Some(-18))
    assert (expectedRange == authMap("augustus").dateRange)
  }

  it should "find a list of authorities for a year value" in {
    val ocre = Ocre(cex, true)
    val auths1 = ocre.authoritiesForYear(-20)
    assert(auths1.size == 1)
    assert(auths1(0)== "augustus")

    val auths2 = ocre.authoritiesForYear(50)
    assert(auths2.isEmpty)

  }
}
