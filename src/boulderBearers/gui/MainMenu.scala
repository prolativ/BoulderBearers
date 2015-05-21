package boulderBearers.gui

import scala.swing.Button
import scala.swing.event.ButtonClicked
import scala.swing.BorderPanel.Position._
import scala.swing.GridPanel
import scala.swing.Label
import scala.swing._

class MainMenu extends GridPanel(2, 1){
	val newGameButton = new Button("New game")
	val tutorialButton = new Button("Tutorial")
	val optionsButton = new Button("Options")
	val quitButton = new Button("Quit")
    val buttons = List(newGameButton, tutorialButton, optionsButton, quitButton)
    val buttonsPanel = new GridPanel(buttons.length, 1)
	buttonsPanel.contents ++= buttons
    listenTo(buttons: _*)
	
	val titleLabel = new Label("Boulder Bearers")
	
	this.contents ++= List(titleLabel, buttonsPanel)
    
	//layout(buttonsPanel) = Center
    //layout(new Label("Boulder Bearers")) = North
	
    reactions += {
		case ButtonClicked(`newGameButton`) =>
			GameFrame.contents = GameFrame.boardChooser
		case ButtonClicked(`tutorialButton`) =>
		case ButtonClicked(`optionsButton`) =>
		case ButtonClicked(`quitButton`) => System.exit(0)
	}
}