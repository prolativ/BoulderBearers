package boulderBearers.gui

import java.awt.Color
import scala.swing.Graphics2D
import java.awt.Dimension

object SimpleBoardPainter extends BoardPainter{
	val distance = 5

	private def paintBase(g: Graphics2D, size:Dimension): Unit = {
		g.setColor(Color.BLACK)
        g.drawRect(0, 0, size.width, size.height)
        g.setColor(Color.WHITE)
        g.fillRect(1, 1, size.width - 1, size.height - 1)
	}
	
	private def paintSelected(g: Graphics2D, size:Dimension, selectionColor: Option[Color]): Unit = {
		val color = selectionColor.getOrElse(Color.WHITE)
		g.setColor(color)
        g.fillRect(1, 1, size.width - 1, size.height - 1)
        g.setColor(Color.WHITE)
        g.fillRect(distance, distance, size.width - 2 * distance, size.height - 2 * distance)
	}
	
	def paintBoulder(g: Graphics2D, size:Dimension, playerColor: Color, selectionColor: Option[Color]): Unit = {
		paintBase(g, size)
		paintSelected(g, size, selectionColor)
		g.setColor(playerColor)
		g.fillOval(2 * distance, 2 * distance, size.width - 4 * distance, size.height - 4 * distance)
	}
	
	def paintStrongman(g: Graphics2D, size:Dimension, playerColor: Color, selectionColor: Option[Color]): Unit = {
		paintBase(g, size)
		paintSelected(g, size, selectionColor)
		g.setColor(playerColor)
		g.fillRect(2 * distance, 2 * distance, size.width - 4 * distance, size.height - 4 * distance)
	}
	
	def paintGuardian(g: Graphics2D, size:Dimension, playerColor: Color, selectionColor: Option[Color]): Unit = {
		paintBase(g, size)
		paintSelected(g, size, selectionColor)
		g.setColor(playerColor)
		val xs = Array(2 * distance, size.width / 2,  size.width - 2 * distance, size.width / 2)
        val ys = Array(size.height / 2, 2 * distance, size.height /2, size.height - 2 * distance)
        g.fillPolygon(xs, ys, xs.length)
	}
	
	def paintTrap(g: Graphics2D, size:Dimension): Unit = {
		paintBase(g, size)
		g.setColor(Color.BLACK)
		g.drawLine(0, 0, size.width, size.height)
		g.drawLine(size.width, 0, 0, size.height)
	}
	
	def paintTrapped(g: Graphics2D, size:Dimension, playerColor: Color): Unit = {
		paintBase(g, size)
		g.setColor(playerColor)
		g.fillOval(2 * distance, 2 * distance, size.width - 4 * distance, size.height - 4 * distance)
		g.setColor(Color.BLACK)
		g.drawRect(0, 0, size.width, size.height)
		g.drawLine(0, 0, size.width, size.height)
		g.drawLine(size.width, 0, 0, size.height)
	}
	def paintEmpty(g: Graphics2D, size:Dimension): Unit = {
		paintBase(g, size)
	}
	def paintOutOfBoard(g: Graphics2D, size:Dimension): Unit = {
		g.setColor(Color.BLACK)
        g.fillRect(0, 0, size.width, size.height)
	}

}