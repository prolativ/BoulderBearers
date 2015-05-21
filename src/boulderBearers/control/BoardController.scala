package boulderBearers.control


import boulderBearers.gameCore.Board

trait BoardController{
	val board: Board
    
    def takeTurn()
	
}