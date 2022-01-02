package uk.co.inops.sudoku

import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.system.measureTimeMillis

internal class BruteForceAlgorithmTest {

  @ParameterizedTest
  @MethodSource("difficultExamples")
  fun canSolveSimpleSudoku(example: List<List<Int>>) {
    val sudoku = Sudoku(example)
    try {
      val compositeAlgorithm =
        CompositeAlgorithm(sudoku, setOf(NakedSingleCellAlgorithm(sudoku), HiddenSingleCellAlgorithm(sudoku)))
      val algo = BruteForceAlgorithm(sudoku, compositeAlgorithm)

      val time = measureTimeMillis {
        algo.trySolve()
      }
      println("Solved in $time milliseconds")
      sudoku.solvedCount shouldBe sudoku.size * sudoku.size
      sudoku.rows.all { row -> row.all { cell -> cell.possibleValues.isEmpty() } } shouldBe true
      sudoku.print()
    } catch (e: Throwable) {
      sudoku.print()
      throw e
    }
  }

  companion object {
    @JvmStatic
    fun difficultExamples(): List<List<List<Int>>> {
      return listOf(
        listOf(
          listOf(0, 0, 0, 6, 0, 0, 4, 0, 0),
          listOf(7, 0, 0, 0, 0, 3, 6, 0, 0),
          listOf(0, 0, 0, 0, 9, 1, 0, 8, 0),
          listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
          listOf(0, 5, 0, 1, 8, 0, 0, 0, 3),
          listOf(0, 0, 0, 3, 0, 6, 0, 4, 5),
          listOf(0, 4, 0, 2, 0, 0, 0, 6, 0),
          listOf(9, 0, 3, 0, 0, 0, 0, 0, 0),
          listOf(0, 2, 0, 0, 0, 0, 1, 0, 0)
        ),
        listOf(
          listOf(2, 0, 0, 3, 0, 0, 0, 0, 0),
          listOf(8, 0, 4, 0, 6, 2, 0, 0, 3),
          listOf(0, 1, 3, 8, 0, 0, 2, 0, 0),
          listOf(0, 0, 0, 0, 2, 0, 3, 9, 0),
          listOf(5, 0, 7, 0, 0, 0, 6, 2, 1),
          listOf(0, 3, 2, 0, 0, 6, 0, 0, 0),
          listOf(0, 2, 0, 0, 0, 9, 1, 4, 0),
          listOf(6, 0, 1, 2, 5, 0, 8, 0, 9),
          listOf(0, 0, 0, 0, 0, 1, 0, 0, 2)
        ),
        listOf(
          listOf(0, 2, 0, 0, 0, 0, 0, 0, 0),
          listOf(0, 0, 0, 6, 0, 0, 0, 0, 3),
          listOf(0, 7, 4, 0, 8, 0, 0, 0, 0),
          listOf(0, 0, 0, 0, 0, 3, 0, 0, 2),
          listOf(0, 8, 0, 0, 4, 0, 0, 1, 0),
          listOf(6, 0, 0, 5, 0, 0, 0, 0, 0),
          listOf(0, 0, 0, 0, 1, 0, 7, 8, 0),
          listOf(5, 0, 0, 0, 0, 9, 0, 0, 0),
          listOf(0, 0, 0, 0, 0, 0, 0, 4, 0),
        ),
        listOf(
          listOf(0, 0, 9, 4, 0, 0, 0, 5, 0),
          listOf(5, 0, 0, 0, 7, 0, 8, 0, 0),
          listOf(3, 0, 0, 8, 0, 0, 0, 1, 0),
          listOf(0, 0, 0, 1, 0, 0, 0, 8, 0),
          listOf(7, 0, 0, 0, 4, 0, 0, 0, 2),
          listOf(0, 2, 0, 0, 0, 3, 0, 0, 0),
          listOf(0, 1, 0, 0, 0, 5, 0, 0, 3),
          listOf(0, 0, 4, 0, 6, 0, 0, 0, 1),
          listOf(0, 6, 0, 0, 0, 4, 9, 0, 0),
        ),
        listOf(
          listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
          listOf(0, 0, 0, 0, 0, 3, 0, 8, 5),
          listOf(0, 0, 1, 0, 2, 0, 0, 0, 0),
          listOf(0, 0, 0, 5, 0, 7, 0, 0, 0),
          listOf(0, 0, 4, 0, 0, 0, 1, 0, 0),
          listOf(0, 9, 0, 0, 0, 0, 0, 0, 0),
          listOf(5, 0, 0, 0, 0, 0, 0, 7, 3),
          listOf(0, 0, 2, 0, 1, 0, 0, 0, 0),
          listOf(0, 0, 0, 0, 4, 0, 0, 0, 9)
        )
      )
    }
  }
}