package reinforcementlearning

data class Move(
    val row: Int,
    val column: Int,
    var value: Double,
) {
    constructor(row: Int, column: Int) :
            this(row, column, Double.NEGATIVE_INFINITY)

    override fun toString(): String {
        return "Move{row=${row + 1}, column=${column + 1}, value=${value}}"
    }

    companion object {
        fun getIncreasingComparator(): Comparator<Move> {
            return Comparator { o1: Move, o2: Move ->
                return@Comparator o1.value.compareTo(o2.value)
            }
        }

        fun getDecreasingComparator(): Comparator<Move> {
            return Comparator { o1: Move, o2: Move ->
                return@Comparator -o1.value.compareTo(o2.value)
            }
        }

        fun max(m1: Move?, m2: Move?): Move? {
            if (m1 == null) return m2
            if (m2 == null) return m1
            if (m1.value > m2.value) {
                return m1
            }
            return m2
        }

        fun min(m1: Move?, m2: Move?): Move? {
            if (m1 == null) return m2
            if (m2 == null) return m1
            if (m1.value < m2.value) {
                return m1
            }
            return m2
        }
    }
}
