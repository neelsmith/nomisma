package edu.holycross.shot.nomisma

import com.esri.core.geometry._


case class MintPoint(mint: String, pt: Point) 

/**
*/
case class ContentsGraph (
  id: String,
  hoard: Point,
  mintPoints: Vector[MintPoint]
)
