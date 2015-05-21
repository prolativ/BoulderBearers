package boulderBearers.gameCore

import scala.io.Source
import scala.io.BufferedSource
import boulderBearers.exceptions.GameException


class Board(val name: String, board: Array[Array[Field]], val playersCount: Int, winningCondition: WinningCondition) {
    private val bouldersCounts = new Array[Int](playersCount)
    for(i <- 0 until playersCount)
        bouldersCounts(i) = countFields(Boulder(i))
        
    def playerBoulders(i: Int) = bouldersCounts(i)
    
	override def toString: String = name
    
	def countFields(field: Field): Int = {
		var count = 0;
		for(i <- 0 until width; j <- 0 until height)
			if(board(i)(j) == field) count += 1
		count
	}
    
    def copy(condition: WinningCondition): Board = {
    	val copiedFields = board.map(_.clone)
    	new Board(name, copiedFields, playersCount, condition)
    } 
	
	def apply(x: Int, y: Int): PositionedField = PositionedField(x, y, board(x)(y))
	
	def apply(f: PositionedField): PositionedField = PositionedField(f.x, f.y, board(f.x)(f.y))
	
	
	private def update(x: Int, y: Int, f: Field): Unit = {
		board(x)(y) = f
	}
	
	private def update(oldField: PositionedField, newField: Field): Unit = {
		board(oldField.x)(oldField.y) = newField
	}
	
	def width = board.size
	def height = board(0).size
	
	
	/*private def update(f1: Field, f2: Field): Unit = {
		board(f1.x)(f1.y) = f2 match{
			case BattleField(_, _, content) => BattleField(f1.x, f1.y, content)
			case Trap(_, _, content) => Trap(f1.x, f1.y, content)
		}
	}*/
	
	/*Strongman/guardian(old, new) */
	def move(f1: PositionedField, f2: PositionedField): Unit = {
		this(f1) = EmptyField
		this(f2) = f1
	}
	
	
	/*Strongman, Boulder/Guardian(old, new) */
	def carry(f1: PositionedField, f2: PositionedField, f3: PositionedField): Unit = {
		this(f1) = EmptyField
		this(f3) = f2
		this(f2) = f1
		
	}
	 
	/*Strongman, Boulder, Trap */
	def entrap(f1: PositionedField, f2: PositionedField, f3: PositionedField): Unit = {
        val playerNo = this(f2).field.asInstanceOf[Boulder].playerNo
		this(f3) = Trapped(playerNo)
		this(f2) = f1
		this(f1) = EmptyField
        bouldersCounts(playerNo) -= 1
	}
	
	/*Strongman1(old), Strongman2(old, new) */
	def doubleMove(f1: PositionedField, f2: PositionedField, f3: PositionedField): Unit = {
		this(f1) = EmptyField
		this(f2) = EmptyField
		this(f3) = f2
		val (x, y) = (f2.x - f1.x, f2.y - f1.y) match{
			case (dx, 0) => (f3.x - math.signum(dx), f3.y)
			case (0, dy) => (f3.x, f3.y - math.signum(dy))
		}
		this(x, y) = f1
	}
	
	/*Strongman1(old), Strongman2(old), Boulder/Guardian(old, new) */
	def doubleCarry(f1: PositionedField, f2: PositionedField, f3: PositionedField, f4: PositionedField): Unit = {
		this(f1) = EmptyField
		this(f2) = EmptyField
		this(f3) = f2
		val (x, y) = (f2.x - f1.x, f2.y - f1.y) match{
			case (dx, 0) => (f3.x - math.signum(dx), f3.y)
			case (0, dy) => (f3.x, f3.y - math.signum(dy))
		}
		this(x, y) = f1
		this(f4) = f3
	}
	
	def doubleCarry(f1: PositionedField, f2: PositionedField, f3: PositionedField, f4: PositionedField, f5: PositionedField): Unit = {
		this(f5) = f4
		this(f4) = f3
		this(f3) = f2
		this(f2) = EmptyField
		val (x, y) = (f2.x - f1.x, f2.y - f1.y) match{
			case (dx, 0) => (f3.x - math.signum(dx), f3.y)
			case (0, dy) => (f3.x, f3.y - math.signum(dy))
		}
		this(x, y) = f1
		this(f1) = EmptyField
	}
	
	
	def doubleEntrap(f1: PositionedField, f2: PositionedField, f3: PositionedField, f4: PositionedField): Unit = {
        val playerNo = this(f3).field.asInstanceOf[Boulder].playerNo
        this(f4) = Trapped(playerNo)
		this(f3) = f2
		this(f2) = EmptyField
		val (x, y) = (f2.x - f1.x, f2.y - f1.y) match{
			case (dx, 0) => (f3.x - math.signum(dx), f3.y)
			case (0, dy) => (f3.x, f3.y - math.signum(dy))
		}
		this(x, y) = f1
		this(f1) = EmptyField
        bouldersCounts(playerNo) -= 1
	}
	
	def doubleEntrap(f1: PositionedField, f2: PositionedField, f3: PositionedField, f4: PositionedField, f5: PositionedField): Unit = {
		val playerNo = this(f4).field.asInstanceOf[Boulder].playerNo
        this(f5) = Trapped(playerNo)
		this(f4) = f3
		this(f3) = f2
		this(f2) = EmptyField
		val (x, y) = (f2.x - f1.x, f2.y - f1.y) match{
			case (dx, 0) => (f3.x - math.signum(dx), f3.y)
			case (0, dy) => (f3.x, f3.y - math.signum(dy))
		}
		this(x, y) = f1
		this(f1) = EmptyField
        bouldersCounts(playerNo) -= 1
	}
	
	
	def onWay(f1: PositionedField, f2: PositionedField): List[PositionedField] = (f2.x - f1.x, f2.y - f1.y ) match{
		case (0, 0) => Nil
		case (x, 0) => {
			val step = math.signum(x)
			val list = (f1.x + step until f2.x by step) flatMap(x => board(x)(f1.y) match{
				case EmptyField => None
				case field => Some(PositionedField(x, f1.y, field))
			}) toList;
			list reverse
		}
		case (0, y) => {
			val step = math.signum(y)
			val list = (f1.y + step until f2.y by step) flatMap(y => board(f1.x)(y) match{
				case EmptyField => None
				case field => Some(PositionedField(f1.x, y, field))
			}) toList;
			list reverse
		}
		case _ => throw GameException("Wrong selection - could not determine orientation")
	}
	
	def isGuardedFrom(field: PositionedField, playerNo: Int): Boolean = field.field match {
		case _: Strongman => true
		case _: Boulder => 
			var guarded = false
			for(i <- field.x - 1 to field.x + 1;
				j <- field.y - 1 to field.y + 1){
				this(i, j) match{
					case PosGuardian(g) if g.playerNo != playerNo => guarded = true
					case _ => {}
				}
			}
			guarded
		case _ => false
	}
	
	
	/*def process (msg: Any) = msg match{
		case Board.Move(f1: PositionedField, f2: PositionedField) => move(f1, f2)
		case _ => {}
	}*/
    
    def gameFinished: Boolean = {
        winningCondition(this)
    }
}

	
object Board{
	
	abstract class BoardMessage
	case class Move(f1: PositionedField, f2: PositionedField) extends BoardMessage
	case class Carry(f1: PositionedField, f2: PositionedField, f3: PositionedField) extends BoardMessage
	case class CarryToTrap(f1: PositionedField, f2: PositionedField, f3: PositionedField) extends BoardMessage
	case class DoubleMove(f1: PositionedField, f2: PositionedField, f3: PositionedField) extends BoardMessage
	case class DoubleCarry(f1: PositionedField, f2: PositionedField, f3: PositionedField, f4: PositionedField) extends BoardMessage
	case class DoubleCarryToTrap(f1: PositionedField, f2: PositionedField, f3: PositionedField, f4: PositionedField) extends BoardMessage
	
	def apply(source: BufferedSource): Board = {
		val linesIt = source.getLines()
		val name = linesIt.next
		val width = linesIt.next.toInt
		val height = linesIt.next.toInt
		val playersCount = linesIt.next.toInt
		val winningCondition = FirstDefeated
		//val winningCondition = WinningCondition(linesIt.next)
		val board = Array.ofDim[Field](width, height)
		for(j <- 0 until height){
			val line = linesIt.next
			val numberedFields = line.split("\\s+").zipWithIndex
			for((fieldStr, i) <- numberedFields) board(i)(j) = Field(fieldStr)
		}
		
		new Board(name, board, playersCount, winningCondition)
	}
	
	def apply(filename: String): Board = apply(Source.fromFile(filename))
}
