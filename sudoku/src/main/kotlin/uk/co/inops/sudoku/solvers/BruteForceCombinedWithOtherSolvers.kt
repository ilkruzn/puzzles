package uk.co.inops.sudoku.solvers

import uk.co.inops.sudoku.Cell
import uk.co.inops.sudoku.Sudoku
import uk.co.inops.sudoku.analysers.CompositeAnalysis
import uk.co.inops.sudoku.analysers.HiddenPairPreAnalysis
import uk.co.inops.sudoku.analysers.NakedPairPreAnalysis
import uk.co.inops.sudoku.analysers.PreAnalysis
import java.util.Deque
import kotlin.random.Random

class BruteForceCombinedWithOtherSolvers(
  private val solverAlgorithm: Algorithm,
  private val preAnalysis: PreAnalysis
) : Algorithm {

  constructor() : this(
    CompositeAlgorithm(setOf(NakedSingleCellAlgorithm(), HiddenSingleCellAlgorithm())),
    CompositeAnalysis(setOf(NakedPairPreAnalysis(), HiddenPairPreAnalysis()))
  )

  private val guessedCells: Deque<Cell> = java.util.ArrayDeque()

  override fun trySolve(sudoku: Sudoku): Boolean {
    var i = 0
    var guessCount = 0
    preAnalysis.analyse(sudoku)
    while (!sudoku.hasBeenSolved() && i++ < 100000) {
      val solvedCount = sudoku.solvedCount
      solverAlgorithm.trySolve(sudoku)
      if (sudoku.hasBeenSolved()) {
        break
      }

      if (solvedCount < sudoku.solvedCount && preAnalysis.analyse(sudoku)) {
        continue
      }

      var rolledBack = false
      do {
        val currentCell = getCellToGuess(sudoku, rolledBack)
        rolledBack = false
        if (currentCell == null) {
          //println("No cells left to guess, rolling back.")
          rollback(sudoku)
          rolledBack = true
          continue
        }

        sudoku.beginHistory()
        setCellValue(currentCell, sudoku)
        guessCount++

        if (sudoku.hasConflict) {
          //println("Rolling back after conflict. Current cells solved: ${sudoku.solvedCount}")
          rollback(sudoku)
          //println("Rolled back after conflict. Current cells solved: ${sudoku.solvedCount}")
          rolledBack = true
        }

      } while (rolledBack)

      preAnalysis.analyse(sudoku)
    }
//
//    println("Solved in $i iterations")
//    println("Guess count: $guessCount")
    return true
  }

  private fun setCellValue(currentCell: Cell, sudoku: Sudoku) {
    val value = currentCell.possibleValues.first { !currentCell.guessed.contains(it) }
    //        println("Guessing number $value in cell [${currentCell.row}, ${currentCell.col}]. Possible values: ${currentCell.possibleValues}, index: ${currentCell.currentGuessIndex}")
    sudoku.set(currentCell.row, currentCell.col, value)
    currentCell.guessed.add(value)
//    preAnalysis.analyseRow(sudoku, currentCell.row)
//    preAnalysis.analyseColumn(sudoku, currentCell.col)
//    preAnalysis.analyseBox(currentCell)
  }

  private fun rollback(sudoku: Sudoku) {
    val history = sudoku.endHistory()
    while (history.isNotEmpty()) {
      history.pop().also {
        it.reset()
      }
    }

    if (guessedCells.size > 0) {
      val lastGuessedCell = guessedCells.peek()
      if (lastGuessedCell.guessed.size == lastGuessedCell.possibleValues.size) {
        guessedCells.pop().also { it.guessed.clear() }
        if (sudoku.hasHistory()) {
          rollback(sudoku) // rollback again as there isn't any numbers left to guess in the last one
        }
      }
    }

    sudoku.hasConflict = false
  }

  private fun getCellToGuess(sudoku: Sudoku, conflict: Boolean): Cell? {

    val lastGuessedCell: Cell? = guessedCells.peek()
    if (conflict && lastGuessedCell != null && lastGuessedCell.guessed.size < lastGuessedCell.possibleValues.size) {
      return lastGuessedCell
    }

    val allEmptyCells = sudoku.rows
      .flatMap {
        it.filter { cell ->
          !guessedCells.contains(cell)
              && cell.value == 0
              && cell.guessed.size < cell.possibleValues.size
              && cell.possibleValues.size > 1
        }
      }
      .sortedBy { it.possibleValues.size - it.guessed.size }
      .take(3)

    if (allEmptyCells.isEmpty()) {
      return null
    }

    val emptyCell = allEmptyCells[Random.nextInt(allEmptyCells.size)]

    return emptyCell.also {
      guessedCells.push(it)
    }
  }
}