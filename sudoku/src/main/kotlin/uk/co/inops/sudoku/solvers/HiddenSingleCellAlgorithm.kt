package uk.co.inops.sudoku.solvers

import uk.co.inops.sudoku.Cell
import uk.co.inops.sudoku.Sudoku

class HiddenSingleCellAlgorithm() : Algorithm {

  override fun trySolve(sudoku: Sudoku): Boolean {
    do {
      val currentSolvedCount = sudoku.solvedCount
      (1..sudoku.size).forEach { value ->
        with(sudoku) {
          rows.forEach { row -> findCellAndSet(sudoku, value, row) }
          columns.forEach { col -> findCellAndSet(sudoku, value, col) }
          boxes.forEach { box -> findCellAndSet(sudoku, value, box) }
        }
      }
    } while (sudoku.solvedCount > currentSolvedCount && !sudoku.hasBeenSolved())

    return sudoku.hasBeenSolved()
  }

  private fun findCellAndSet(sudoku: Sudoku, value: Int, group: List<Cell>) {
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