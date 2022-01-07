package uk.co.inops.sudoku

import java.util.Deque


class Sudoku(
  val size: Int,
  private val preAnalysis: PreAnalysis
) {

  private val history: Deque<Deque<Cell>> = java.util.ArrayDeque()
  var hasConflict = false
  var solvedCount = 0
    internal set

  constructor(initialValues: List<List<Int>>, preAnalysis: PreAnalysis) : this(9, preAnalysis) {
    for (row in initialValues.indices) {
      for (col in initialValues[row].indices) {
        set(row, col, initialValues[row][col], false)
      }
    }
    preAnalysis.analyse(this)
  }

  private val totalCells = size * size

  val rows = (0 until size).map { row ->
    (0 until size).map { col ->
      Cell(row, col, this)
    }
  }

  fun hasBeenSolved() = solvedCount == totalCells

  fun set(row: Int, col: Int, value: Int, preAnalysis: Boolean = true) {
    rows[row][col].value = value

    if (preAnalysis) {
      this.preAnalysis.analyse(this)
    }

    if (history.isNotEmpty()) {
      history.peek().push(rows[row][col])
    }
  }

  fun beginHistory() = history.push(java.util.ArrayDeque())

  fun endHistory(): Deque<Cell> = history.pop()

  fun hasHistory() = history.isNotEmpty()

  val columns: List<List<Cell>> by lazy {

    (0 until size).map { j ->
      val column = mutableListOf<Cell>()
      val top = rows[0][j]
      var current = top
      do {
        column.add(current)
        current = current.down
      } while (top != current)
      column
    }
  }

  val boxes: List<List<Cell>> by lazy {
    (0 until size).map { i ->
      val box = mutableListOf<Cell>()
      val top = rows[i / 3 * 3][i % 3 * 3] // top left cell of a box
      var current = top
      do {
        box.add(current)
        current = current.boxNext
      } while (top != current)
      box
    }
  }


  fun print() {
    for (row in rows) {
      for (cell in row) {
        print("${cell.value} ")
      }
      println()
    }
  }

}