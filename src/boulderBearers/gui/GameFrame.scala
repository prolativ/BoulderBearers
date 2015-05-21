package boulderBearers.gui

import scala.swing.MainFrame

object GameFrame extends MainFrame {
	val mainMenu = new MainMenu
	val boardChooser = new BoardChooser
	
	title = "Boulder Bearers"
	contents = mainMenu
    maximize()
}