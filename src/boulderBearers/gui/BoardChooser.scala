package boulderBearers.gui

import scala.swing.BorderPanel
import scala.swing.BorderPanel.Position._
import scala.swing.TextField
import scala.swing.Label
import scala.swing.ComboBox
import scala.swing.Button
import scala.swing.GridPanel
import scala.swing.event.ButtonClicked
import scala.swing.event.SelectionChanged
import java.io.File
import scala.swing.ListView
import scala.swing.ListView.IntervalMode
import scala.swing.ScrollPane
import scala.swing.RadioButton
import scala.swing.ButtonGroup
import boulderBearers.gameCore.Board
import boulderBearers.gameCore.FirstDefeated
import boulderBearers.gameCore.AllDefeated

class BoardChooser extends BorderPanel{
	
	def onSelectionChanged(): Unit = choice match{
		case Some(board) =>
			previewPanel.layout(new BoardPreview(board, SimpleBoardPainter)) = Center
			widthLabel.text = "Width: " + board.width
			heightLabel.text = "Height: " + board.height
			playersCountLabel.text = "No. of players: " + board.playersCount
		case _ => {}
    }
	
	def choice: Option[Board] = {
        if(boardSelector.selection.items.length == 1)
            Some(boardSelector.selection.items(0))
        else
            None
    }
	
	def winningCondition = winningConditionButtons.selected match{
		case Some(`firstDefeatedButton`) => FirstDefeated
		case Some(`allDefeatedButton`) => AllDefeated
		case _ => FirstDefeated
	}
	
	private val dir = new File("res/boards")
	val boards = dir.listFiles.flatMap(f =>
        try{
            Some(Board("res/boards/" + f.getName))
        }catch{
            case _: Exception => None
        }
    )
	
	val boardSelector = new ListView(boards) {
		selection.intervalMode = IntervalMode.Single
		selectIndices(0)
	}
	
	val selectionPanel = new BorderPanel{
		
		layout(new Label("Board type:")) = North
		layout(new ScrollPane(boardSelector)) = Center
	}
	
	val widthLabel = new Label("Width: ")
	val heightLabel = new Label("Height: ")
	val playersCountLabel = new Label("No. of players: ")
	val winningConditionLabel = new Label("Winning condition:")
	val firstDefeatedButton = new RadioButton("first defeated")
	val allDefeatedButton = new RadioButton("all defeated")
	val winningConditionButtons = new ButtonGroup(firstDefeatedButton, allDefeatedButton)
	winningConditionButtons.select(firstDefeatedButton)
	val winningConditionPanel = new GridPanel(1, 3){
		contents ++= List(winningConditionLabel, firstDefeatedButton, allDefeatedButton)
	}
    
	
	val previewPanel = new BorderPanel{
		val parameterPanel = new GridPanel(4,1){
			contents ++= List(widthLabel, heightLabel, playersCountLabel, winningConditionPanel)
		}
		layout(parameterPanel) = South
	}
	
	val backButton = new Button("<<")
	val forwardButton = new Button(">>")
	
	val navigationPanel = new GridPanel(1, 2){
		contents ++= List(backButton, forwardButton)
	}
	
	layout(selectionPanel) = West
	layout(previewPanel) = Center
	layout(navigationPanel) = South
	
	listenTo(backButton, forwardButton, boardSelector.selection)
	reactions += {
		case ButtonClicked(`backButton`) =>
			GameFrame.contents = GameFrame.mainMenu
		case ButtonClicked(`forwardButton`) => choice match{
			case Some(board) =>
				GameFrame.contents = new PlayerCreator
			case _ => {}
		}
		case SelectionChanged(`boardSelector`) => onSelectionChanged()
	}
	
	onSelectionChanged()
	
}