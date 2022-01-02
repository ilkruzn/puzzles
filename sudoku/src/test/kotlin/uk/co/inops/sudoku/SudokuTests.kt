package uk.co.inops.sudoku

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

internal class SudokuTests {

  @Test
  fun initialisesCells() {
    val size = 9
    val sudoku = Sudoku(size)
    sudoku.rows shouldNotBe null
    sudoku.rows.size shouldBe size
    for (row in sudoku.rows) {
      row.size shouldBe 9
    }
  }

  @Test
  fun initialisesCellsWithRowNeighborhood() {
    val size = 9
    val sudoku = Sudoku(size)

    sudoku.rows[0][0].right shouldBe sudoku.rows[0][1]
    sudoku.rows[0][size - 1].right shouldBe sudoku.rows[0][0]
    sudoku.rows[4][4].right shouldBe sudoku.rows[4][5]
    sudoku.rows[size - 1][size - 1].right shouldBe sudoku.rows[size - 1][0]
  }

  @Test
  fun initialisesCellsWithColumnNeighborhood() {
    val size = 9
    val sudoku = Sudoku(size)

    sudoku.rows[0][0].down shouldBe sudoku.rows[1][0]
    sudoku.rows[0][size - 1].down shouldBe sudoku.rows[1][size - 1]
    sudoku.rows[3][3].down shouldBe sudoku.rows[4][3]
    sudoku.rows[size - 1][size - 1].down shouldBe sudoku.rows[0][size - 1]
  }

  @Test
  fun initialisesCellsWithBoxNeighborhood() {
    val size = 9
    val sudoku = Sudoku(size)

    sudoku.rows[0][0].boxNext shouldBe sudoku.rows[0][1]
    sudoku.rows[0][2].boxNext shouldBe sudoku.rows[1][0]
    sudoku.rows[2][2].boxNext shouldBe sudoku.rows[0][0]

    sudoku.rows[3][3].boxNext shouldBe sudoku.rows[3][4]
    sudoku.rows[3][5].boxNext shouldBe sudoku.rows[4][3]
    sudoku.rows[5][5].boxNext shouldBe sudoku.rows[3][3]

    sudoku.rows[6][6].boxNext shouldBe sudoku.rows[6][7]
    sudoku.rows[6][8].boxNext shouldBe sudoku.rows[7][6]
    sudoku.rows[8][8].boxNext shouldBe sudoku.rows[6][6]
  }


  @Test
  fun initialisesAllCellsWithFullPossibleValues() {
    val size = 9
    val sudoku = Sudoku(size)
    val expectedPossibleValues = (1..size).toList()

    for (row in sudoku.rows) {
      for (cell in row) {
        cell.possibleValues.shouldContainExactly(expectedPossibleValues)
      }
    }
  }

  @Test
  fun setCellValuesClearsPossibleValues() {
    val size = 9
    val sudoku = Sudoku(size)

    sudoku.rows[6][5].value = 3

    sudoku.rows[6][5].possibleValues.shouldBeEmpty()
  }

  @Test
  fun setCellValueRecalculatesPossibleValuesInRow() {
    val size = 9
    val cellNewValue = 5
    val sudoku = Sudoku(size)

    val expectedPossibleValues = (1..size).filter { it != cellNewValue }
    val modifiedCell = sudoku.rows[0][0]
    var nextCell = modifiedCell.right

    modifiedCell.value = cellNewValue

    while (nextCell != modifiedCell) {
      nextCell.possibleValues.shouldContainExactly(expectedPossibleValues)
      nextCell = nextCell.right
    }
  }

  @Test
  fun setCellValueDoesNotRecalculateOtherRows() {
    val size = 9
    val cellNewValue = 5
    val sudoku = Sudoku(size)

    val expectedPossibleValues = (1..9).toList()
    val modifiedCell = sudoku.rows[0][0]
    modifiedCell.value = cellNewValue

    sudoku.rows[3][1].possibleValues.shouldContainExactly(expectedPossibleValues)
    sudoku.rows[size - 1][2].possibleValues.shouldContainExactly(expectedPossibleValues)
    sudoku.rows[size - 1][size - 1].possibleValues.shouldContainExactly(expectedPossibleValues)
  }

  @Test
  fun setCellValueRecalculatesPossibleValuesInColumn() {
    val size = 9
    val cellNewValue = 5
    val sudoku = Sudoku(size)

    val expectedPossibleValues = (1..size).filter { it != cellNewValue }
    val modifiedCell = sudoku.rows[size - 1][0]
    var nextCell = modifiedCell.down
    modifiedCell.value = cellNewValue

    while (nextCell != modifiedCell) {
      nextCell.possibleValues.shouldContainExactly(expectedPossibleValues)
      nextCell = nextCell.down
    }
  }

  @Test
  fun setCellValueDoesNotRecalculateOtherColumns() {
    val size = 9
    val cellNewValue = 5
    val sudoku = Sudoku(size)

    val expectedPossibleValues = (1..9).toList()
    val modifiedCell = sudoku.rows[0][0]
    modifiedCell.value = cellNewValue

    sudoku.rows[1][3].possibleValues.shouldContainExactly(expectedPossibleValues)
    sudoku.rows[2][size - 1].possibleValues.shouldContainExactly(expectedPossibleValues)
    sudoku.rows[size - 1][size - 1].possibleValues.shouldContainExactly(expectedPossibleValues)
  }

  @Test
  fun setCellValueRecalculatesPossibleValuesInABox() {
    val size = 9
    val cellNewValue = 3
    val sudoku = Sudoku(size)

    val expectedPossibleValues = (1..size).filter { it != cellNewValue }
    val modifiedCell = sudoku.rows[4][4]
    var nextCell = modifiedCell.boxNext
    modifiedCell.value = cellNewValue

    while (nextCell != modifiedCell) {
      nextCell.possibleValues.shouldContainExactly(expectedPossibleValues)
      nextCell = nextCell.boxNext
    }
  }

  @Test
  fun resetCellValueRecalculatesPossibleValuesInSameCellRowColumnAndBox() {
    val size = 9
    val sudoku = Sudoku(size)
    val cellToReset = sudoku.rows[0][1]

    sudoku.rows[0][0].value = 1
    sudoku.rows[1][1].value = 2
    sudoku.rows[2][2].value = 3

    cellToReset.value = 4 // Here 4 will be removed from possible values in other empty cells
    cellToReset.value = 0 // This should put 4 back into those cells' possible values


    // the modified cell's possible values should be recalculated based on the values set in its
    // neighbors in the same row, column and box.
    // all empty cells in the same row, column and box with the one reset to zero must be updated
    // so that number is 4 removed from their possible values
    cellToReset.possibleValues shouldBe (4..9).toList()

    var rowNext = cellToReset.right
    var columnNext = cellToReset.down
    var boxNext = cellToReset.boxNext
    while (rowNext != cellToReset) {
      if (rowNext.value == 0) {
        rowNext.possibleValues shouldContain 4
      }

      if (columnNext.value == 0) {
        columnNext.possibleValues shouldContain 4
      }

      if (boxNext.value == 0) {
        boxNext.possibleValues shouldContain 4
      }

      rowNext = rowNext.right
      columnNext = columnNext.down
      boxNext = boxNext.boxNext
    }
  }

  @Test
  fun setValueIncrementsSolvedCount() {
    val sudoku = Sudoku(9)
    sudoku.rows[0][0].value = 5
    sudoku.rows[4][4].value = 3
    sudoku.rows[8][8].value = 9

    sudoku.solvedCount shouldBe 3
  }
  @Test
  fun setValueDoesNotIncrementsSolvedCountIfPreviousValueIsNotZero() {
    val sudoku = Sudoku(9)
    sudoku.rows[0][0].value = 5
    sudoku.rows[4][4].value = 3
    sudoku.rows[8][8].value = 9

    sudoku.solvedCount shouldBe 3
    sudoku.rows[8][8].value = 8
    sudoku.solvedCount shouldBe 3

  }

  @Test
  fun resetValueDecrementsSolvedCount() {
    val sudoku = Sudoku(9)
    sudoku.rows[0][0].value = 5
    sudoku.rows[4][4].value = 3
    sudoku.rows[8][8].value = 9

    sudoku.rows[0][0].value = 0
    sudoku.rows[4][4].value = 0
    sudoku.rows[8][8].value = 0


    sudoku.solvedCount shouldBe 0
  }

  @Test
  fun resetValueDoesNotDecrementsSolvedCountIfPreviousValueWasAlsoZero() {
    val sudoku = Sudoku(9)
    sudoku.rows[0][0].value = 5
    sudoku.rows[4][4].value = 3
    sudoku.rows[8][8].value = 9
    sudoku.rows[0][5].value = 0


    sudoku.solvedCount shouldBe 3
  }
}
