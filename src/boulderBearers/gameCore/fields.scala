package boulderBearers.gameCore

trait Field

trait Piece extends Field{
	def playerNo: Int
}

case class Boulder(playerNo: Int) extends Piece

case class Guardian(playerNo: Int) extends Piece

case class Strongman(playerNo: Int) extends Piece

case object EmptyField extends Field

case object Trap extends Field

case class Trapped(playerNo: Int) extends Field

case object OutOfBoard extends Field



//Coordinate wrappers

case class PositionedField(x: Int, y: Int, field: Field)

object PositionedField{
	implicit def asField(pf: PositionedField): Field = pf.field
}

object PosBoulder{
	def unapply(pf: PositionedField) = pf.field match{
		case b: Boulder => Some(b)
		case _ => None
	}
}

object PosGuardian{
	def unapply(pf: PositionedField) = pf.field match{
		case g: Guardian => Some(g)
		case _ => None
	}
}

object PosStrongman{
	def unapply(pf: PositionedField) = pf.field match{
		case s: Strongman => Some(s)
		case _ => None
	}
}

object PosEmpty{
	def unapply(pf: PositionedField) = pf.field match{
		case EmptyField => true
		case _ => false
	}
}

object PosTrap{
	def unapply(pf: PositionedField) = pf.field match{
		case Trap => true
		case _ => false
	}
}

object PosTrapped{
	def unapply(pf: PositionedField) = pf.field match{
		case t: Trapped => Some(t)
		case _ => None
	}
}

//For reading from file represetation

object Field{
	def apply(str: String): Field = str match{
		case "-" => EmptyField
		case "$" => OutOfBoard
		case "@" => Trap
		case GuardianStr(playerNo) => Guardian(playerNo)
		case StrongmanStr(playerNo) => Strongman(playerNo)
		case BoulderStr(playerNo) => Boulder(playerNo)
	}
}

object GuardianStr{
	def unapply(str: String) = {
		if(str(0) == 'g') Some(str.substring(1, str.length).toInt) else None 
	}
}

object StrongmanStr{
	def unapply(str: String) = {
		if(str(0) == 's') Some(str.substring(1, str.length).toInt) else None 
	}
}

object BoulderStr{
	def unapply(str: String) = {
		if(str(0) == 'b') Some(str.substring(1, str.length).toInt) else None 
	}
}
