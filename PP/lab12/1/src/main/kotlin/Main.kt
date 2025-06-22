fun main() {
    val initialList = listOf(1, 21, 75, 39, 7, 2, 35, 3, 31, 7, 8)

    val filteredList = initialList.filter { it >= 5 }
    println("After removing numbers < 5: $filteredList")

    val pairedList = filteredList.chunked(2)
    println("Grouped in pairs: $pairedList")

    val multipliedPairs = pairedList.map { pair ->
        if (pair.size == 2) {
            pair[0] * pair[1]
        } else {
            pair[0]
        }
    }
    println("Multiplied pairs: $multipliedPairs")

    val finalSum = multipliedPairs.sum()
    println("Final sum: $finalSum")
}