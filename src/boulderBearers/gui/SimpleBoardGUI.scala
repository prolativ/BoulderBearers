package boulderBearers.gui

import scala.Array
import scala.swing.Button
import scala.swing.GridPanel
import scala.swing.Graphics2D
import scala.swing.event.ButtonClicked
import java.awt.Color
import scala.swing.Label
import scala.swing.BorderPanel
import scala.swing.BorderPanel.Position._
import scala.swing.Dialog
import boulderBearers.gameCore.Game
import boulderBearers.gameCore.Board
import boulderBearers.gameCore.Player
import boulderBearers.gameCore.Piece
import boulderBearers.gameCore.Trapped
import boulderBearers.gameCore.PositionedField

class SimpleBoardGUI(override val game: Game, override val painter: BoardPainter) extends BorderPanel with BoardGUI {
    val players: List[Player] = game.players
    val board: Board = game.board
    
   	def showWinnerMessage(player: Player): Unit = {
    	repaint
    	Dialog.showMessage(this, "The winner is " + player.name, "Congratulations!")
    }
    
	private case class FieldButton(x: Int, y: Int) extends Button{
		var marked = false
        
        override def paint(g: Graphics2D){
			val field = board(x, y).field
			
			val (playerColor, selectionColor) = field match{
				case piece: Piece =>
					if(marked) 
						(players(piece.playerNo).color, Some(players(game.curPlayerNo).color))
					else
						(players(piece.playerNo).color, None)
				case trapped: Trapped =>
					(players(trapped.playerNo).color, None)
				case _ =>
					(Color.WHITE, None)
			}
			
			painter.paintField(field, g, size, playerColor, selectionColor)
			
		}
        
	}
	
	def setPlayer(player: Player): Unit = {
		playerLabel.text = player.name + "'s turn"
		playerLabel.foreground = player.color
	}
	
	private val playerLabel = new Label("")
	private val grid = new GridPanel(board.width, board.height)
	
	private val buttons = Array.ofDim[FieldButton](board.width, board.height)
	
	for(i <- 0 until board.width;
		j <- 0 until board.height){
			val button = new FieldButton(j, i)
			buttons(i)(j) = button
			grid.contents += button
			listenTo(button)
	}
	
	layout(playerLabel) = North
	layout(grid) = Center
	
    reactions += { case ButtonClicked(FieldButton(x, y)) =>
    	game.guiController.selectField(board(x, y))
    	peer.repaint()
    }
    
    def mark(f: PositionedField): Unit = {
    	buttons(f.y)(f.x).marked = true
    }
    
    def unmark(f: PositionedField): Unit = {
    	buttons(f.y)(f.x).marked = false
    }
    
    
    def mark(x: Int, y: Int): Unit = {
    	buttons(y)(x).marked = true
    }
    
    def unmark(x: Int, y: Int): Unit = {
    	buttons(y)(x).marked = false
    }
}