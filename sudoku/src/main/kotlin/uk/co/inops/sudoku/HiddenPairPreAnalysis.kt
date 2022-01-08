package uk.co.inops.sudoku

class HiddenPairPreAnalysis : PreAnalysis {

  override fun analyse(sudoku: Sudoku) {
    with(sudoku) {
      rows.forEach(::analyse)
      columns.forEach(::analyse)
      boxes.forEach(::analyse)
    }
  }

  override fun analyseRow(sudoku: Sudoku, row: Int) {
    analyse(sudoku.rows[row])
  }

  override fun analyseColumn(sudoku: Sudoku, column: Int) {
    analyse(sudoku.columns[column])
  }

  override fun analyseBox(box: List<Cell>) {
    analyse(box)
  }

  private fun analyse(group: List<Cell>) {
    // this can be a method parameter, but seems like trying for did not make the overall resolution time any faster
    val pairCount = 2
    val valueMap = mutableMapOf<Int, List<Cell>>()
    for (value in 1..group.size) {
      val cellsWithValue = group.filter { it.possibleValues.contains(value) }
      if (cellsWithValue.size == pairCount) {
        valueMap[value] = cellsWithValue
      }
    }

    if (valueMap.size < pairCount) return

    val processed = mutableSetOf<Int>()
    for (entry in valueMap) {

      if (entry.key in processed) continue

      val pair1 = entry.key
      processed.add(pair1)
      val otherPairs = valueMap
        .filter { it.key != entry.key && it.value.intersect(entry.value).size == pairCount }
        .map { it.key }

      if (otherPairs.size == pairCount - 1) {
        valueMap[pair1]?.forEach { cell ->
          cell.possibleValues.removeIf { it != pair1 && !otherPairs.contains(it) }
        }
      }

      processed.addAll(otherPairs)
    }
  }
}
