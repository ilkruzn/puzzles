package uk.co.inops.sudoku

class HiddenSingleCellAlgorithm(private val sudoku: Sudoku) : Algorithm {

  override fun trySolve(): Boolean {
    do {
      val currentSolvedCount = sudoku.solvedCount
      (1..sudoku.size).forEach { value ->
        sudoku.rows.forEach { row -> findCellAndSet(value, row) }
        sudoku.columns.forEach { col -> findCellAndSet(value, col) }
        sudoku.boxes.forEach { box -> findCellAndSet(value, box) }
      }
    } while (sudoku.solvedCount > currentSolvedCount && !sudoku.hasBeenSolved())

    return sudoku.hasBeenSolved()
  }

  private fun findCellAndSet(value: Int, group: List<Cell>) {
    val cells = mutableListOf<Cell>()
    group.forEach { currentCell ->
      if (currentCell.possibleValues.contains(value)) {
        cells.add(currentCell)
      }
    }

    if (cells.size == 1) {
      val hiddenCell = cells.first() // value only appears on this cell's possible values set
      sudoku.set(hiddenCell.row, hiddenCell.col, value)
    }
  }
}