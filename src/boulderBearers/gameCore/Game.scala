package boulderBearers.gameCore

import boulderBearers.control.SimpleGUIController
import boulderBearers.gui.GameFrame
import boulderBearers.gui.BoardGUI
import boulderBearers.gui.SimpleBoardGUI
import boulderBearers.gui.SimpleBoardPainter

class Game(val board: Board, val players: List[Player]){
    val gui: BoardGUI = new SimpleBoardGUI(this, SimpleBoardPainter)
    val guiController = new SimpleGUIController(this);
    
    val controllers = players.map(player => player match{
        case _: HumanPlayer => guiController;
    })
    
    var curPlayerNo = players.length - 1
    turnFinished()
    
    def finishTurn(): Unit = {
        turnFinished()
    }
    
    def turnFinished(): Unit = {
        if(board.gameFinished){
        	gui.showWinnerMessage(players(curPlayerNo))
            GameFrame.contents = GameFrame.mainMenu
        }
        curPlayerNo = (curPlayerNo + 1) % players.length
        gui.setPlayer(players(curPlayerNo))
    }
}

