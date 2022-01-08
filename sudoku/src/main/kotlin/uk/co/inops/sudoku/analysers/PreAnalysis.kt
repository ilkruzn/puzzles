package uk.co.inops.sudoku.analysers

import uk.co.inops.sudoku.Cell
import uk.co.inops.sudoku.Sudoku

interface PreAnalysis {
  fun analyse(sudoku: Sudoku): Boolean

  fun analyseRow(sudoku: Sudoku, row: Int) = analyseGroup(sudoku.rows[row])

  fun analyseColumn(sudoku: Sudoku, col: Int) = analyseGroup(sudoku.columns[col])

  fun analyseBox(cell: Cell) = analyseGroup(cell.box)

  fun analyseGroup(group: List<Cell>): Boolean

}
