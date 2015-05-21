package boulderBearers.gameCore

import java.awt.Color


abstract class Player(val name: String, val color: Color)

case class HumanPlayer(override val name: String, override val color: Color) extends Player(name, color)
case class ComputerPlayer(override val name: String, override val color: Color) extends Player(name, color)
