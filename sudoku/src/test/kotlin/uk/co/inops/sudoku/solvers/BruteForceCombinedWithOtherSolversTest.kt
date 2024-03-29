package uk.co.inops.sudoku.solvers

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import uk.co.inops.sudoku.Sudoku
import uk.co.inops.sudoku.analysers.CompositeAnalysis
import uk.co.inops.sudoku.analysers.HiddenPairPreAnalysis
import uk.co.inops.sudoku.analysers.NakedPairPreAnalysis
import uk.co.inops.sudoku.analysers.PointingPairPreAnalysis
import kotlin.system.measureTimeMillis

internal class BruteForceCombinedWithOtherSolversTest {

  @ParameterizedTest
  @MethodSource("difficultExamples")
  fun canSolveSimpleSudoku(example: List<List<Int>>) {
    val compositeAnalysis =
      CompositeAnalysis(
        setOf(
          NakedPairPreAnalysis(),
          HiddenPairPreAnalysis(),
          PointingPairPreAnalysis()
        )
      )
    val compositeAlgorithm = CompositeAlgorithm(
      setOf(NakedSingleCellAlgorithm(), HiddenSingleCellAlgorithm())
    )
    val sudoku = Sudoku(example)
    val algo = BruteForceCombinedWithOtherSolvers(compositeAlgorithm, compositeAnalysis)
    try {
      val time = measureTimeMillis {
        algo.trySolve(sudoku)
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

  @Test
  fun measureSpeed() {
    val example = difficultExamples()[7]
    val compositeAnalysis =
      CompositeAnalysis(
        setOf(
          NakedPairPreAnalysis(),
          HiddenPairPreAnalysis(),
          PointingPairPreAnalysis()
        )
      )

    val compositeAlgorithm = CompositeAlgorithm(
      setOf(NakedSingleCellAlgorithm(), HiddenSingleCellAlgorithm())
    )
    val algo = BruteForceCombinedWithOtherSolvers(compositeAlgorithm, compositeAnalysis)

    val time = measureTimeMillis {
      repeat(100) {
        val sudoku = Sudoku(example)
        algo.trySolve(sudoku)
      }
    }

    println("Total $time milliseconds")

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
        // Evil
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
        // Evil 2
        listOf(
          listOf(0, 0, 0, 6, 5, 0, 0, 1, 0),
          listOf(0, 0, 7, 0, 0, 0, 0, 0, 0),
          listOf(8, 2, 0, 0, 0, 9, 3, 0, 0),
          listOf(0, 0, 4, 0, 0, 0, 5, 0, 0),
          listOf(0, 0, 3, 0, 0, 7, 0, 0, 0),
          listOf(5, 7, 0, 9, 0, 0, 0, 0, 6),
          listOf(0, 0, 0, 0, 8, 0, 0, 0, 3),
          listOf(9, 5, 0, 0, 0, 2, 8, 0, 0),
          listOf(4, 0, 0, 0, 0, 0, 0, 0, 0),
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
        ),
        listOf(
          listOf(6, 0, 0, 0, 0, 0, 0, 0, 7),
          listOf(2, 0, 9, 0, 0, 0, 3, 0, 4),
          listOf(5, 4, 0, 9, 3, 0, 2, 0, 0),
          listOf(4, 2, 0, 0, 8, 7, 9, 0, 0),
          listOf(0, 9, 6, 1, 5, 0, 0, 0, 0),
          listOf(0, 5, 0, 0, 9, 4, 0, 0, 0),
          listOf(9, 0, 0, 0, 0, 8, 0, 0, 5),
          listOf(0, 6, 5, 0, 0, 9, 0, 2, 0),
          listOf(0, 0, 0, 0, 0, 0, 0, 1, 9)
        ),
        // Most evil: https://www.conceptispuzzles.com/index.aspx?uri=info/article/424
        listOf(
          listOf(8, 0, 0, 0, 0, 0, 0, 0, 0),
          listOf(0, 0, 3, 6, 0, 0, 0, 0, 0),
          listOf(0, 7, 0, 0, 9, 0, 2, 0, 0),
          listOf(0, 5, 0, 0, 0, 7, 0, 0, 0),
          listOf(0, 0, 0, 0, 4, 5, 7, 0, 0),
          listOf(0, 0, 0, 1, 0, 0, 0, 3, 0),
          listOf(0, 0, 1, 0, 0, 0, 0, 6, 8),
          listOf(0, 0, 8, 5, 0, 0, 0, 1, 0),
          listOf(0, 9, 0, 0, 0, 0, 4, 0, 0)
        ),
        listOf(
          listOf(1, 0, 0, 0, 0, 7, 0, 9, 0),
          listOf(0, 3, 0, 0, 2, 0, 0, 0, 8),
          listOf(0, 0, 9, 6, 0, 0, 5, 0, 0),
          listOf(0, 0, 5, 3, 0, 0, 9, 0, 0),
          listOf(0, 1, 0, 0, 8, 0, 0, 0, 2),
          listOf(6, 0, 0, 0, 0, 4, 0, 0, 0),
          listOf(3, 0, 0, 0, 0, 0, 0, 1, 0),
          listOf(0, 4, 0, 0, 0, 0, 0, 0, 7),
          listOf(0, 0, 7, 0, 0, 0, 3, 0, 0)
        )
      )
    }
  }
}
