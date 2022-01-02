package uk.co.inops.sudoku

class NakedSingleCellAlgorithm(private val sudoku: Sudoku) : Algorithm {

  override fun trySolve(): Boolean {
    do {
      val currentSolvedCount = sudoku.solvedCount

      //try to find a cell with only 1 possible value and set its value to that
      for (row in sudoku.rows) {
        for (cell in row) {
          if (cell.possibleValues.size == 1) {
            sudoku.set(cell.row, cell.col, cell.possibleValues.first())
          }
        }
      }

    } while (sudoku.solvedCount > currentSolvedCount && !sudoku.hasBeenSolved())

    return sudoku.hasBeenSolved()
  }

}