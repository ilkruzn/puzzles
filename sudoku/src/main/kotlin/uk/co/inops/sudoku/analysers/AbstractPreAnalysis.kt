package uk.co.inops.sudoku.analysers

import uk.co.inops.sudoku.Sudoku

abstract class AbstractPreAnalysis : PreAnalysis {
  override fun analyse(sudoku: Sudoku): Boolean {
    var result = false
    with(sudoku) {
      rows.forEach { result = result || analyseGroup(it) }
      columns.forEach { result = result || analyseGroup(it) }
      boxes.forEach { result = result || analyseGroup(it) }
    }
    return result
  }
}