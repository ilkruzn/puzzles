package uk.co.inops.sudoku.analysers

import uk.co.inops.sudoku.Cell

class NakedPairPreAnalysis : AbstractPreAnalysis() {

  override fun analyseGroup(group: List<Cell>): Boolean {
    return analyseGroup(group, 2) || analyseGroup(group, 3) || analyseGroup(group, 4)
  }

  private fun analyseGroup(
    group: List<Cell>,
    pairCount: Int
  ): Boolean {
    var found = false
    group
      .filter { it.possibleValues.size == pairCount }
      .groupBy { Tuples(it.possibleValues.elementAt(0), it.possibleValues.elementAt(1)) }
      .filter { it.value.size == pairCount }
      .forEach {
        found = true
        for (cell in group) {
          if (!it.value.contains(cell)) {
            cell.possibleValues.removeAll(it.key.toSet())
          }
        }
      }
    return found
  }
}
