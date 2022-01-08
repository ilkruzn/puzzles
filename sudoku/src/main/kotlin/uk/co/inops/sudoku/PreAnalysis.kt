package uk.co.inops.sudoku

interface PreAnalysis {
  fun analyse(sudoku: Sudoku)
  fun analyseRow(sudoku: Sudoku, row: Int)
  fun analyseColumn(sudoku: Sudoku, column: Int)
  fun analyseBox(box: List<Cell>)
}
