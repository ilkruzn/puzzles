package uk.co.inops.sudoku.solvers

import uk.co.inops.sudoku.Sudoku

interface Algorithm {
  fun trySolve(sudoku: Sudoku): Boolean
}