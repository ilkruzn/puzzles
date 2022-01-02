package uk.co.inops.sudoku

class Cell(val row: Int, val col: Int, private val sudoku: Sudoku) {

  private var previousValue = 0

  var currentGuessIndex = -1
  val down: Cell by lazy { sudoku.rows[(row + 1) % sudoku.size][col] }
  val right: Cell by lazy { sudoku.rows[row][(col + 1) % sudoku.size] }
  val boxNext: Cell by lazy {
    var nextCol = col + 1
    if (nextCol % 3 == 0) {
      nextCol -= 3
    }

    var nextRow = row
    if (nextCol < col) {
      nextRow++
      if (nextRow % 3 == 0) {
        nextRow -= 3
      }
    }

    sudoku.rows[nextRow][nextCol]
  }

  private val initialPossibleValues = (1..sudoku.size).shuffled()
  val possibleValues: MutableSet<Int> by lazy { initialPossibleValues.toMutableSet() }
  internal var value: Int = 0
    set(value) {
      previousValue = field
      field = value
      recalculate()
    }

  fun reset() {
    value = previousValue
    previousValue = 0
  }

  private fun recalculateRow() {
    var current = this.right
    while (current != this) {
      recalculateCell(current)
      current = current.right
    }
  }

  private fun recalculate() {

    if (value != 0 && previousValue == 0) {
      sudoku.solvedCount++
    }

    if (value == 0 && previousValue != 0) {
      sudoku.solvedCount--
    }

    resetPossibleValues()
    recalculateRow()
    recalculateColumn()
    recalculateBox()
  }

  private fun resetPossibleValues() {
    if (value == 0) {
      possibleValues.addAll(initialPossibleValues)
    } else {
      possibleValues.clear()
    }
  }


  private fun recalculateColumn() {
    var current = this.down
    while (current != this) {
      recalculateCell(current)
      current = current.down
    }
  }

  private fun recalculateBox() {
    var current = this.boxNext
    while (current != this) {
      recalculateCell(current)
      current = current.boxNext
    }
  }

  private fun recalculateCell(other: Cell) {
    if (this.value == 0 && other.value != 0) {
      this.possibleValues.remove(other.value)
      if (this.possibleValues.isEmpty()) {
        sudoku.hasConflict = true
      }
    }

    if (this.value != 0 && other.value == 0) {
      other.possibleValues.remove(this.value)
      if (other.possibleValues.isEmpty()) {
        sudoku.hasConflict = true
      }
    }

    if (this.value == 0 && this.previousValue != 0 && other.value == 0) {
      other.recalculate()
    }
  }

}
