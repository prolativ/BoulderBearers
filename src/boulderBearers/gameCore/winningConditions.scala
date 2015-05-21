package boulderBearers.gameCore

abstract trait WinningCondition{
    def apply(board: Board): Boolean
}

object WinningCondition{
	def apply(str: String): WinningCondition = str match{
		case "first defeated" => FirstDefeated
		case "all defeated" => AllDefeated
	}
}

case object FirstDefeated extends WinningCondition{
    def apply(board: Board): Boolean = (0 until board.playersCount) exists(x => board.playerBoulders(x) <= 0)
}

case object AllDefeated extends WinningCondition{
    def apply(board: Board): Boolean = {
        val playersStillInGame = (0 until board.playersCount) map(x => 
            board.playerBoulders(x) match{
                case 0 => 0
                case _ => 1
            }) reduce(_ + _)
        playersStillInGame == 1
    }
}