package boulderBearers.control

import scala.collection.immutable.Stack
import boulderBearers.gameCore.Game
import boulderBearers.gameCore.Board
import boulderBearers.gui.BoardGUI
import boulderBearers.exceptions.GameException
import boulderBearers.gameCore.PositionedField
import boulderBearers.gameCore.Strongman
import boulderBearers.gameCore.PosEmpty
import boulderBearers.gameCore.PosGuardian
import boulderBearers.gameCore.PosStrongman
import boulderBearers.gameCore.PosBoulder
import boulderBearers.gameCore.PosTrap
import boulderBearers.gameCore.Piece
import boulderBearers.gameCore.Guardian

class SimpleGUIController(override val game: Game) extends GUIController{
	val board: Board = game.board
    val gui: BoardGUI = game.gui
	
	private var selectionStack: Stack[List[PositionedField]] = Stack(List())
	
	def selectField(field: PositionedField): Unit = try {
        val playerNo = game.curPlayerNo
        
		val selection: List[PositionedField] = selectionStack.head match{
			case Nil => field :: Nil
			case (head @ PositionedField(_, _, s:Strongman)) :: rest => List(field) ++  board.onWay(head, field) ++ List(head) ++ rest
			case list => field :: list
		}
		
		selection match{
			//2 elements ///////////////////////////////////////
			
			case (f1 @ PosEmpty()) :: (f2 @ PosGuardian(_)) :: Nil
					if  (math.abs(f1.x - f2.x) <= 1 && math.abs(f1.y - f2.y) <= 1) => {
				gui.unmark(f2)
				selectionStack = Stack(List())
				board.move(f2, f1)
                game.turnFinished()
			}
					
			case (f1 @ PosEmpty()) :: (f2 @ PosStrongman(_)) :: Nil => {
				gui.unmark(f2)
				selectionStack = Stack(List())
				board.move(f2, f1)
				game.turnFinished()
			}
			
			case (f1 @ (PosBoulder(_) | PosGuardian(_))) :: (f2 @ PosStrongman(_)) :: Nil
					if !board.isGuardedFrom(f1, playerNo) => {
				gui.mark(f1)
				selectionStack +:= selection
			}
			
					
			//3 elements ///////////////////////////////////////	
					
			case (f1 @ PosEmpty()) :: (f2 @ (PosBoulder(_) | PosGuardian(_))) :: (f3 @ PosStrongman(_)) :: Nil
					if !board.isGuardedFrom(f2, playerNo) &&  (math.abs(f1.x - f2.x) <= 1 && math.abs(f1.y - f2.y) <= 1) => {
				gui.unmark(f2)
				gui.unmark(f3)
				selectionStack = Stack(List())
				board.carry(f3, f2, f1)
				game.turnFinished()
			}
					
			case (f1 @ PosStrongman(_)) :: (f2 @ (PosBoulder(_) | PosGuardian(_))) :: (f3 @ PosStrongman(_)) :: Nil
					if !board.isGuardedFrom(f2, playerNo) &&  (math.abs(f1.x - f2.x) <= 1 && math.abs(f1.y - f2.y) <= 1) && f1 == f3 => {
				gui.unmark(f2)
				gui.unmark(f3)
				selectionStack = Stack(List())
				board.carry(f3, f2, f1)
				game.turnFinished()
			}
					
			case (f1 @ PosTrap()) :: (f2 @ PosBoulder(b)) :: (f3 @ PosStrongman(_)) :: Nil
					if !board.isGuardedFrom(f2, playerNo) && (f1.x == f3.x || f1.y == f3.y) && b.playerNo != playerNo=> {
				gui.unmark(f2)
				gui.unmark(f3)
				selectionStack = Stack(List())
				board.entrap(f3, f2, f1)
				game.turnFinished()
			}
				
			
			case (f1 @ PosEmpty()) :: (f2 @ PosStrongman(_)) :: (f3 @ PosStrongman(_)) :: Nil => {
				gui.unmark(f3)
				selectionStack = Stack(List())
				board.doubleMove(f3, f2, f1)
				game.turnFinished()
			}

			case (f1 @ (PosBoulder(_) | PosGuardian(_))) :: (f2 @ PosStrongman(_)) :: (f3 @ PosStrongman(_)) :: Nil => {
				gui.mark(f1)
				selectionStack +:= selection
			}
			
			
			//4 elements ///////////////////////////////////////
			
			
			case (f1 @ PosEmpty()) :: (f2 @ (PosBoulder(_) | PosGuardian(_))) :: (f3 @ PosStrongman(_)) :: (f4 @ PosStrongman(_)) :: Nil => {
				gui. unmark(f2)
				gui.unmark(f4)
				selectionStack = Stack(List())
				board.doubleCarry(f4, f3, f2, f1)
				game.turnFinished()
			}
			
			case (f1 @ PosStrongman(_)) :: (f2 @ (PosBoulder(_) | PosGuardian(_))) :: (f3 @ PosStrongman(_)) :: (f4 @ PosStrongman(_)) :: Nil
					if f1 == f4 && !board.isGuardedFrom(f2, playerNo) => {
				gui. unmark(f2)
				gui.unmark(f4)
				selectionStack = Stack(List())
				board.doubleCarry(f4, f3, f2, f1)
				game.turnFinished()
			}
					
			case (f1 @ PosTrap()) :: (f2 @ PosBoulder(b)) :: (f3 @ PosStrongman(_)) :: (f4 @ PosStrongman(_)) :: Nil
					if !board.isGuardedFrom(f2, playerNo) && (f1.x == f3.x || f1.y == f3.y) && b.playerNo != playerNo=> {
				gui.unmark(f2)
				gui.unmark(f4)
				selectionStack = Stack(List())
				board.doubleEntrap(f4, f3, f2, f1)
				game.turnFinished()
			}
					
			
			case (f1 @ PosEmpty()) :: (f2 @ PosStrongman(_)) :: (f3 @ PosStrongman(_)) :: (f4 @ PosStrongman(_)) :: Nil
					if math.abs(f1.x - f2.x) == 1 || math.abs(f1.y - f2.y) == 1=> {
				gui.unmark(f4)
				selectionStack = Stack(List())
				board.doubleCarry(f4, f3, f2, f1)
				game.turnFinished()
			}
					
					
			//5 elements ///////////////////////////////////////		
					
					
			case (f1 @ PosEmpty()) :: (f2 @ (PosBoulder(_) | PosGuardian(_))) :: (f3 @ (PosBoulder(_) | PosGuardian(_)))
					:: (f4 @ PosStrongman(_)) :: (f5 @ PosStrongman(_)) :: Nil
					if !board.isGuardedFrom(f2, playerNo) && !board.isGuardedFrom(f3, playerNo)
					&& ((math.abs(f1.x - f2.x) == 1 && math.abs(f2.x - f3.x) == 1) || (math.abs(f1.y - f2.y) == 1 && math.abs(f2.y - f3.y) == 1))=> {
				gui.unmark(f5)
				selectionStack = Stack(List())
				board.doubleCarry(f5, f4, f3, f2, f1)
				game.turnFinished()
			}
					
			case (f1 @ PosTrap()) :: (f2 @ PosBoulder(b)) :: (f3 @ (PosBoulder(_) | PosGuardian(_)))
					:: (f4 @ PosStrongman(_)) :: (f5 @ PosStrongman(_)) :: Nil
					if !board.isGuardedFrom(f2, playerNo) && !board.isGuardedFrom(f3, playerNo)
					&& ((math.abs(f1.x - f2.x) == 1 && math.abs(f2.x - f3.x) == 1) || (math.abs(f1.y - f2.y) == 1 && math.abs(f2.y - f3.y) == 1))
					&& b.playerNo != playerNo=> {
				gui.unmark(f2)
				gui.unmark(f3)
				selectionStack = Stack(List())
				board.doubleEntrap(f5, f4, f3, f2, f1)
				game.turnFinished()
			}
			
			
		
			//general ///////////////////////////////////////
					
					
			case (f @ PositionedField(_, _, p: Piece)) :: Nil
					if p.playerNo == playerNo && (p.isInstanceOf[Guardian] | p.isInstanceOf[Strongman]) => {
				gui.mark(f)
				selectionStack +:= selection
			}
			
			case f1 :: f2 :: rest  if f1 == f2 => {
				gui.unmark(f1)
				selectionStack = selectionStack.pop
			}
		
			case _ => {}
		
		}
		
	} catch{
		case GameException(_) => {}
	}
	
}
