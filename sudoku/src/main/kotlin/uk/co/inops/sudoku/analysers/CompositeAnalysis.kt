package uk.co.inops.sudoku.analysers

import uk.co.inops.sudoku.Cell

class CompositeAnalysis(private val analysers: Set<AbstractPreAnalysis>) : AbstractPreAnalysis() {

  override fun analyseGroup(group: List<Cell>): Boolean {
    var result = false
    analysers.forEach { result = result || it.analyseGroup(group) }
    return result
  }

}