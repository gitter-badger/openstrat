/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */package rich
package pCiv
import geom._
import pGrid._
import pStrat._
import pEarth._
class Warrior(val faction: Faction, var x: Int, var y: Int) extends Lunit
{ 
   override def equals(other: Any): Boolean = other match
   {
      case that: Warrior => faction == that.faction
      case _ => false
   }
   var movePts: Int = 10
   def terrCost(tile: CTile): Int = tile.terr match
   {
      case Mountains(_) => 10
      case Hills(_) => 6
      case _ => 4
   }
}

object Warrior
{
   def apply(polity: Faction, cood: Cood): Warrior = new Warrior(polity, cood.x, cood.y)
   def apply(polity: Faction, x: Int, y: Int): Warrior = new Warrior(polity, x, y)
}