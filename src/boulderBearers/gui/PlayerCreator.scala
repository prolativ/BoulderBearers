package boulderBearers.gui

import scala.swing.BorderPanel
import scala.swing.BorderPanel.Position._
import scala.swing.Button
import scala.swing.GridPanel
import scala.swing.event.ButtonClicked
import java.awt.Color
import scala.swing.TextField
import scala.swing.ListView
import scala.swing.Label
import scala.swing.Alignment
import scala.swing.Swing
import scala.swing.Graphics2D
import scala.swing.ComboBox
import boulderBearers.gameCore.Game
import boulderBearers.gameCore.Player
import boulderBearers.gameCore.HumanPlayer

class PlayerCreator extends BorderPanel{
    
    private class ColorLabel(var color: Color) extends Label{
        override def paint(g: Graphics2D): Unit = {
            g.setColor(color)
            g.fillRect(0, 0, size.width, size.height)
        }
    }
    
    private class ColorBox[A](index: Int) extends ComboBox(colors){
        renderer = new ListView.AbstractRenderer[Color, ColorLabel](new ColorLabel(colors(index))) {
            def configure(list: ListView[_], isSelected: Boolean, focused: Boolean, color: Color, index: Int) {
                component.color = color
                component.text = "color"
                component.xAlignment = Alignment.Center
                if(isSelected) {
                    component.border = Swing.LineBorder(list.selectionBackground, 3)
                } else {
                    component.border = Swing.EmptyBorder(3)
                }
            }
        }
    }
    
	val board = GameFrame.boardChooser.choice.get.copy(
			GameFrame.boardChooser.winningCondition
	)
	val playersCount = board.playersCount
    
    private val colors: List[Color] = List(
        new Color(255, 0, 0),
        new Color(0, 255, 0),
        new Color(0, 0, 255),
        new Color(255, 255, 0),
        new Color(255, 0, 255),
        new Color(0, 255, 255)
    )
	
	private val nameFields = (0 until playersCount) map (x => new TextField("Player" + x))
	private val colorBoxes = (0 until playersCount) map (x => new ColorBox(x))
	
	
	private val titlesPanel = new GridPanel(1, 2){
		contents ++= List(new Label("Player's nick"), new Label("Player's color"))
	}
	layout(titlesPanel) = North
	
	private val playersPanel = new GridPanel(playersCount, 2)
	
	for(i <- 0 until playersCount){
		colorBoxes(i).selection.item = colors(i)
		playersPanel.contents += nameFields(i)
		playersPanel.contents += colorBoxes(i)
	}
	
	layout(playersPanel) = Center
	
	private val backButton = new Button("<<")
	private val forwardButton = new Button(">>")
	
	private val navigationPanel = new GridPanel(1, 2){
		contents ++= List(backButton, forwardButton)
	}
	
	layout(navigationPanel) = South
	
	listenTo(backButton, forwardButton)
	
	reactions += {
        case ButtonClicked(`backButton`) =>
			GameFrame.contents = GameFrame.boardChooser
		case ButtonClicked(`forwardButton`) =>
			
			val players: List[Player] = (0 until playersCount) map(x => new HumanPlayer(
				nameFields(x).text, 
				colorBoxes(x).selection.item
			)) toList
		
			val game = new Game(board, players)
			
			GameFrame.contents = game.gui
	}
}