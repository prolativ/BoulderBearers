package boulderBearers.gui

import java.awt.Color
import scala.swing.Graphics2D
import java.awt.Dimension
import boulderBearers.gameCore.Field
import boulderBearers.gameCore.Piece
import boulderBearers.gameCore.Boulder
import boulderBearers.gameCore.Strongman
import boulderBearers.gameCore.Guardian
import boulderBearers.gameCore.Trap
import boulderBearers.gameCore.Trapped
import boulderBearers.gameCore.EmptyField
import boulderBearers.gameCore.OutOfBoard

trait BoardPainter {
	
	def paintField(field: Field, g: Graphics2D, size:Dimension, playerColor: Color, selectionColor: Option[Color]): Unit = {
		field match{

			case piece: Piece =>{                    
                
                piece match{
                    case _: Boulder =>
                    	paintBoulder(g, size, playerColor, selectionColor)                          
                    case _: Strongman =>
                    	paintStrongman(g, size, playerColor, selectionColor)
                    case _: Guardian =>
                    	paintGuardian(g, size, playerColor, selectionColor)
                }
			}
			
			case Trap =>
				paintTrap(g, size)
				
			case t: Trapped  =>
				paintTrapped(g, size, playerColor)
			
			case EmptyField =>
				paintEmpty(g, size)
                
			case OutOfBoard =>
				paintOutOfBoard(g, size)
				
			case _ => {}
		}
	}
	
	def paintBoulder(g: Graphics2D, size:Dimension, playerColor: Color, selectionColor: Option[Color]): Unit
	def paintStrongman(g: Graphics2D, size:Dimension, playerColor: Color, selectionColor: Option[Color]): Unit
	def paintGuardian(g: Graphics2D, size:Dimension, playerColor: Color, selectionColor: Option[Color]): Unit
	def paintTrap(g: Graphics2D, size:Dimension): Unit
	def paintTrapped(g: Graphics2D, size:Dimension, playerColor: Color): Unit
	def paintEmpty(g: Graphics2D, size:Dimension): Unit
	def paintOutOfBoard(g: Graphics2D, size:Dimension): Unit
}