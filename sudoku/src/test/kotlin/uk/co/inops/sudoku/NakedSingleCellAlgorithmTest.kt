package uk.co.inops.sudoku

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test


internal class NakedSingleCellAlgorithmTest {

  @Test
  fun canFindACellWithOnePossibleValueAndSet() {
    val example = listOf(
      listOf(1, 2, 3, 4, 5, 6, 7, 8, 0),
      listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
      listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
      listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
      listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
      listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
      listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
      listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
      listOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    )

    val sudoku = Sudoku(example)
    val algo = NakedSingleCellAlgorithm(sudoku)

    val solved = algo.trySolve()

    solved shouldBe false
    sudoku.rows[0][8].value shouldBe 9

  }

  @Test
  fun canSolveSimpleSudoku() {
    val example = listOf(
      listOf(1, 2, 3, 4, 5, 6, 7, 8, 0),
      listOf(4, 5, 6, 7, 8, 9, 1, 2, 0),
      listOf(7, 8, 9, 1, 2, 3, 4, 5, 0),
      listOf(2, 3, 4, 5, 6, 7, 8, 9, 0),
      listOf(5, 6, 7, 8, 9, 1, 2, 3, 0),
      listOf(8, 9, 1, 2, 3, 4, 5, 6, 0),
      listOf(3, 4, 5, 6, 7, 8, 9, 1, 0),
      listOf(6, 7, 8, 9, 1, 2, 3, 4, 0),
      listOf(9, 1, 2, 3, 4, 5, 6, 7, 0)
    )

    val sudoku = Sudoku(example)
    val algo = NakedSingleCellAlgorithm(sudoku)

    val solved = algo.trySolve()

    solved shouldBe true
    for(row in sudoku.rows){
      row[8] shouldNotBe 0
    }

  }
}