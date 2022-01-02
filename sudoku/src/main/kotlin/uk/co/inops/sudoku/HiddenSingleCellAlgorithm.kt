package uk.co.inops.sudoku

class HiddenSingleCellAlgorithm(private val sudoku: Sudoku) : Algorithm {

  override fun trySolve(): Boolean {
    do {
      val currentSolvedCount = sudoku.solvedCount
      (1..sudoku.size).forEach { value ->
        solveRows(value)
        solveColumns(value)
        solveBoxes(value)
      }
    } while (sudoku.solvedCount > currentSolvedCount && !sudoku.hasBeenSolved())

    return sudoku.hasBeenSolved()
  }

  private fun solveRows(value: Int) {
    for (row in sudoku.rows) {
      findCellAndSet(row.first(), value) { it.right }
    }
  }

  private fun solveColumns(value: Int) {
    for (i in 0 until sudoku.size) {
      val topCell = sudoku.rows[0][i] //top cell in column i
      findCellAndSet(topCell, value) { it.down }
    }
  }

  private fun solveBoxes(value: Int) {
    for (i in 0 until sudoku.size) {
      val topCell = sudoku.rows[i / 3 * 3][i % 3 * 3] // top left cell of a box
      findCellAndSet(topCell, value) { it.boxNext }
    }
  }

  private fun findCellAndSet(
    startCell: Cell,
    value: Int,
    nextCellProvider: (cell: Cell) -> Cell
  ) {
    val cells = mutableListOf<Cell>()
    var currentCell = startCell
    do {
      if (currentCell.possibleValues.contains(value)) {
        cells.add(currentCell)
      }
      currentCell = nextCellProvider(currentCell)
    } while (currentCell != startCell)

    if (cells.size == 1) {
      val hiddenCell = cells.first() // value only appears on this cell's possible values set
      sudoku.set(hiddenCell.row, hiddenCell.col, value)
    }
  }
}