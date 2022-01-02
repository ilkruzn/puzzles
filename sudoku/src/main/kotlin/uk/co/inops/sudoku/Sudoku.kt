package uk.co.inops.sudoku

import java.util.Deque


class Sudoku(
  val size: Int
) {

  private val history: Deque<Deque<Cell>> = java.util.ArrayDeque()
  var hasConflict = false
  var solvedCount = 0
    internal set

  constructor(initialValues: List<List<Int>>) : this(9) {
    for (row in initialValues.indices) {
      for (col in initialValues[row].indices) {
        set(row, col, initialValues[row][col])
      }
    }
  }

  private val totalCells = size * size

  val rows = (0 until size).map { row ->
    (0 until size).map { col ->
      Cell(row, col, this)
    }
  }

  fun hasBeenSolved() = solvedCount == totalCells

  fun set(row: Int, col: Int, value: Int) {
    rows[row][col].value = value
    if (history.isNotEmpty()) {
      history.peek().push(rows[row][col])
    }
  }

  fun beginHistory() = history.push(java.util.ArrayDeque())

  fun endHistory(): Deque<Cell> = history.pop()

  fun hasHistory() = history.isNotEmpty()

  fun print() {
    for (row in rows) {
      for (cell in row) {
        print("${cell.value} ")
      }
      println()
    }
  }

}