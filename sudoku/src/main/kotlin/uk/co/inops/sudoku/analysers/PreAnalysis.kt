package uk.co.inops.sudoku.analysers

import uk.co.inops.sudoku.Cell
import uk.co.inops.sudoku.Sudoku

interface PreAnalysis {
  fun analyse(sudoku: Sudoku): Boolean
  fun analyseGroup(group: List<Cell>): Boolean
}
