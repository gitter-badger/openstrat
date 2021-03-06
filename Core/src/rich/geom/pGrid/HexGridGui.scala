/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */package rich
package geom
package pGrid
import pDisp._

/** Class for displaying a single hex grid */
abstract class HexGridGui[TileT <: Tile, GridT <: HexGrid[TileT]](val canv: CanvDisp, val grid: GridT) extends
   TileGridGui[TileT, GridT]
{
   override def ptScale = pScale / 4
//   def ofHexsFold[R](f: RegHexOfGrid[TileT] => R, fSum: (R, R) => R, emptyVal: R) =
//   {
//      var acc: R = emptyVal
//      grid.tileCoodForeach{ tileCood =>         
//         val newRes: R = f(RegHexOfGrid[TileT](this, grid, grid.getTile(tileCood)))//{val grid: TileGrid[TileT, SideT]=  thisGrid })
//         acc = fSum(acc, newRes)
//      }
//      acc
//   }
//   
//   def ofHexsDisplayFold(f: RegHexOfGrid[TileT] => Disp2): Disp2 = ofHexsFold[Disp2](f, _ ++ _, Disp2.empty)  
   //override def sideXYVertCoods(x: Int, y: Int): CoodLine = HexGrid.sideXYVertCoods(x, y)
   //override val yRatio: Double = HexCood.yRatio
//   val xRadius: Double = 2 
//   val yRadius: Double = HexCood.yDist2
// 
   //val scaleMin = gridScale / 1000
   //val scaleMax: Dist = gridScale / 10
  // var scale = scaleMax
//   mapFocus = mapCen  
}

//abstract class HexRegGui[TileT <: Tile, SideT <: TileSide](val canv: CanvDisp, val grid: HexGridReg[TileT, SideT]) extends GridGui[TileT, SideT]