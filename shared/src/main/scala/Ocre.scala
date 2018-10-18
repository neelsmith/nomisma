package edu.holycross.shot.nomisma


/** The contents of an edition of OCRE.
*
* @param issues
* @param legends
* @param typeDescriptions
* @param portraits
*/
case class Ocre(issues:  Vector[BasicIssue], legends: Vector[Legend], typeDescriptions: Vector[TypeDescription], portraits: Vector[Portrait])
