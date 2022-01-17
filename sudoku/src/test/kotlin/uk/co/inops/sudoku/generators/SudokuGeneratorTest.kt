package uk.co.inops.sudoku.generators

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import uk.co.inops.sudoku.Sudoku
import uk.co.inops.sudoku.solvers.BruteForceCombinedWithOtherSolvers

internal class SudokuGeneratorTest {

  @Test
  fun generateFromEmpty() {
    val generator = SudokuGenerator()

    val sudoku: Sudoku = generator.generateRandomFull()

    sudoku.hasBeenSolved() shouldBe true
  }

  @Test
  fun newPuzzle() {
    val generator = SudokuGenerator()
    val solver = BruteForceCombinedWithOtherSolvers()

    val puzzle = generator.newPuzzle(60)

    val sudoku = Sudoku(puzzle)
    println("New puzzle")
    sudoku.print()
    sudoku.print(false)
    println()
    val solved = solver.trySolve(sudoku)
    println("Solved puzzle")
    sudoku.print()

    solved shouldBe true

  }


}