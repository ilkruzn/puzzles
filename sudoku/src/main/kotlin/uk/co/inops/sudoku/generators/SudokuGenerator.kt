package uk.co.inops.sudoku.generators

import uk.co.inops.sudoku.Sudoku
import uk.co.inops.sudoku.solvers.BruteForceCombinedWithOtherSolvers
import kotlin.random.Random

class SudokuGenerator {

  fun generateRandomFull(): Sudoku {

    val result = Sudoku(9)
    if (BruteForceCombinedWithOtherSolvers().trySolve(result)) {
      return result
    }
    throw RuntimeException("Could not generate full sudoku.")
  }

  fun newPuzzle(emptyCount: Int): List<List<Int>> = generateRandomFull().let {
    val result = it.rows.map { row -> row.map { cell -> cell.value }.toMutableList() }

    var i = 0
    while (i < emptyCount) {
      val row = Random.Default.nextInt(result.size)
      val col = Random.Default.nextInt(result.size)

      if (result[row][col] != 0) {
        i++
        result[row][col] = 0
      }

    }

    result
  }

}
