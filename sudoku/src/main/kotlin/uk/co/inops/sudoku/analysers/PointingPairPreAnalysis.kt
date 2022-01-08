package uk.co.inops.sudoku.analysers

import uk.co.inops.sudoku.Cell
import uk.co.inops.sudoku.Sudoku

class PointingPairPreAnalysis : AbstractPreAnalysis() {

  override fun analyseRow(sudoku: Sudoku, row: Int) = false
  override fun analyseColumn(sudoku: Sudoku, col: Int) = false

  override fun analyseGroup(group: List<Cell>) =
    analyseCandidate(group, 2) or analyseCandidate(group, 3)

  private fun analyseCandidate(group: List<Cell>, pairSize: Int): Boolean {
    var result = false
    (1..9).forEach { candidate ->
      val cells = group.filter { it.possibleValues.contains(candidate) }
      if (cells.size == pairSize) {
        result = result or checkRow(candidate, cells) or checkColumn(candidate, cells)
      }
    }
    return result
  }

  private fun checkRow(candidate: Int, cells: List<Cell>): Boolean {
    var result = false
    val first = cells.first()
    if (cells.all { it.row == first.row }) {
      println("pointing pair of size ${cells.size} found")
      var current = first.right
      do {
        if (!cells.contains(current)) {
          result = current.possibleValues.remove(candidate) || result
        }
        current = current.right
      } while (current != first)
    }

    return result
  }

  private fun checkColumn(candidate: Int, cells: List<Cell>): Boolean {
    var result = false
    val first = cells.first()
    if (cells.all { it.col == first.col }) {
      var current = first.down
      do {
        if (!cells.contains(current)) {
          result = current.possibleValues.remove(candidate) || result
        }
        current = current.down
      } while (current != first)
    }

    return result
  }
}