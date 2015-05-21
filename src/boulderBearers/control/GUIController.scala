package boulderBearers.control

import boulderBearers.gameCore.Game
import boulderBearers.gameCore.PositionedField

trait GUIController extends BoardController {
	val game: Game
	def selectField(field: PositionedField): Unit
    def takeTurn(): Unit = {}
}