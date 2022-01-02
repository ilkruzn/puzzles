package uk.co.inops.sudoku

import java.util.Deque
import kotlin.random.Random

class BruteForceAlgorithm(
  private val sudoku: Sudoku,
  private val helperAlgorithm: Algorithm
) {

  private val guessedCells: Deque<Cell> = java.util.ArrayDeque()

  fun trySolve(): Boolean {
    var i = 0
    while (!sudoku.hasBeenSolved() && i++ < 100000) {
      helperAlgorithm.trySolve()
      if (sudoku.hasBeenSolved()) {
        break
      }

      var rolledBack = false
      do {
        val currentCell = getCellToGuess(rolledBack)
        rolledBack = false
        if (currentCell == null) {
          //println("No cells left to guess, rolling back.")
          rollback()
          rolledBack = true
          continue
        }

        saveState()
        val value = currentCell.possibleValues.elementAt(currentCell.currentGuessIndex)
//        println("Guessing number $value in cell [${currentCell.row}, ${currentCell.col}]. Possible values: ${currentCell.possibleValues}, index: ${currentCell.currentGuessIndex}")
        sudoku.set(currentCell.row, currentCell.col, value)

        if (sudoku.hasConflict) {
          //println("Rolling back after conflict. Current cells solved: ${sudoku.solvedCount}")
          rollback()
          //println("Rolled back after conflict. Current cells solved: ${sudoku.solvedCount}")
          rolledBack = true
        }

      } while (rolledBack)
    }

    println("Solved in $i iterations")

    return true
  }

  private fun rollback() {
    //println("Guess size ${guessedCells.size}")
    if (sudoku.hasHistory()) {
      val history = sudoku.endHistory()
      while (history.isNotEmpty()) {
        history.pop().also {
          it.reset()
          //println("Reset cell [${it.row}, ${it.col}] to ${it.value}. Possible values: ${it.possibleValues} and index: ${it.currentGuessIndex}")
        }
      }
    }

    if (guessedCells.size > 0) {
      val lastGuessedCell = guessedCells.peek()
      if (lastGuessedCell.currentGuessIndex >= lastGuessedCell.possibleValues.size - 1) {
        guessedCells.pop().also { it.currentGuessIndex = -1 }
        if (sudoku.hasHistory()) {
          rollback() // rollback again as there isn't any numbers left to guess in the last one
        }
      }
    }

    sudoku.hasConflict = false
  }

  private fun saveState() {
    sudoku.beginHistory()
  }

  private fun getCellToGuess(conflict: Boolean): Cell? {

    val lastGuessedCell: Cell? = guessedCells.peek()
    if (conflict && lastGuessedCell != null && lastGuessedCell.value == 0 && lastGuessedCell.currentGuessIndex < lastGuessedCell.possibleValues.size - 1) {
      lastGuessedCell.currentGuessIndex++
      return lastGuessedCell
    }

    val allEmptyCells = sudoku.rows
      .flatMap {
        it.filter { cell ->
          !guessedCells.contains(cell)
                  && cell.value == 0
                  && cell.currentGuessIndex < cell.possibleValues.size - 1
                  && cell.possibleValues.size > 1
        }
      }
      .sortedBy { it.possibleValues.size - it.currentGuessIndex }

    if (allEmptyCells.isEmpty()) {
      return null
    }

//    val emptyCell = allEmptyCells[0]
    val emptyCell = allEmptyCells[Random.nextInt(0, 3.coerceAtMost(allEmptyCells.size))]

    return emptyCell.also {
      it.currentGuessIndex++
      guessedCells.push(it)
    }
  }
}