package boulderBearers.gui

import scala.swing.Component
import scala.swing.Dialog
import boulderBearers.gameCore.Game
import boulderBearers.gameCore.Player
import boulderBearers.gameCore.PositionedField

trait BoardGUI extends Component{
	val game: Game
	val painter: BoardPainter
	
	def setPlayer(player: Player): Unit
	
	def mark(f: PositionedField): Unit
    def unmark(f: PositionedField): Unit
	
	def mark(x: Int, y: Int): Unit
	def unmark(x: Int, y: Int): Unit
	
	def showWinnerMessage(player: Player): Unit
	
}