package uk.co.inops.sudoku.analysers

data class Tuples(
  val t1: Int,
  val t2: Int,
  val t3: Int? = null,
  val t4: Int? = null,
) {
  fun toSet(): Set<Int> {
    val result = mutableSetOf(t1, t2)
    t3?.also { result.add(t3) }
    t4?.also { result.add(t4) }
    return result
  }
}