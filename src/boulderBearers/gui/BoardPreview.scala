package boulderBearers.gui

import scala.swing.Component
import java.awt.Graphics2D
import java.awt.Color
import scala.swing.GridPanel
import boulderBearers.gameCore.Board
import boulderBearers.gameCore.Piece
import boulderBearers.gameCore.Trapped

class BoardPreview(board: Board, painter: BoardPainter) extends GridPanel(board.width, board.height){
	
	private val colors: List[Color] = List(
        new Color(255, 0, 0),
        new Color(0, 255, 0),
        new Color(0, 0, 255),
        new Color(255, 255, 0),
        new Color(255, 0, 255),
        new Color(0, 255, 255)
    )
	
	private case class FieldView(x: Int, y:Int) extends Component{
		override def paint(g: Graphics2D): Unit = {
			
			val field = board(x, y).field
			
			val (playerColor, selectionColor) = field match{
				case piece: Piece =>
					(colors(piece.playerNo), None)
				case trapped: Trapped =>
					(colors(trapped.playerNo), None)
				case _ =>
					(Color.WHITE, None)
			}
			
			painter.paintField(field, g, size, playerColor, selectionColor)
		}	
	}
	
	for(i <- 0 until board.width;
		j <- 0 until board.height){
			contents += new FieldView(j, i)
	}
	
}