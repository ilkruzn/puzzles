package uk.co.inops.sudoku.solvers

import uk.co.inops.sudoku.PreAnalysis
import uk.co.inops.sudoku.Sudoku

class CompositeAlgorithm(
  private val preAnalysis: PreAnalysis,
  private val algorithms: Set<Algorithm>
) : Algorithm {
  override fun trySolve(sudoku: Sudoku): Boolean {
    do {
      val currentSolvedCount = sudoku.solvedCount

      preAnalysis.analyse(sudoku)
      for (algorithm in algorithms) {
        val solved = algorithm.trySolve(sudoku)
//        println("After ${algorithm.javaClass.simpleName} total ${sudoku.solvedCount} cells were solved")
        if (solved) break

      }

    } while (sudoku.solvedCount > currentSolvedCount && !sudoku.hasBeenSolved())

    return sudoku.hasBeenSolved()
  }
}