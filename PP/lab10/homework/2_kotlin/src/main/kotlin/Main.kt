import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() = runBlocking {
    val numbers = listOf(5, 2, 9, 1, 7, 3)
    val alpha = 3

    // Create channels for communication between stages
    val multiplyChannel = Channel<List<Int>>()
    val sortChannel = Channel<List<Int>>()

    // Launch pipeline stages as coroutines

    // Stage 1: Multiply all elements by alpha
    launch {
        println("Stage 1: Multiplying all elements by $alpha")
        val multiplied = numbers.map { it * alpha }
        multiplyChannel.send(multiplied)
        multiplyChannel.close()
    }

    // Stage 2: Sort the elements
    launch {
        val multipliedNumbers = multiplyChannel.receive()
        println("Stage 2: Sorting elements")
        val sorted = multipliedNumbers.sorted()
        sortChannel.send(sorted)
        sortChannel.close()
    }

    // Stage 3: Display the elements
    launch {
        val sortedNumbers = sortChannel.receive()
        println("Stage 3: Displaying elements")
        println("Final result: $sortedNumbers")
    }
}