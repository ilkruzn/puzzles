package uk.co.inops.sudoku.analysers

import uk.co.inops.sudoku.Sudoku

abstract class AbstractPreAnalysis : PreAnalysis {
  override fun analyse(sudoku: Sudoku): Boolean {
    var result = false
    with(sudoku) {
      rows.indices.forEach { result = result || analyseRow(sudoku, it) }
      columns.indices.forEach { result = result || analyseColumn(sudoku, it) }
      boxes.forEach { result = result || analyseBox(it.first()) }
    }
    return result
  }
}