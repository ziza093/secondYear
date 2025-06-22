import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayDeque

fun main() = runBlocking {
    // Queue with different values of n
    val queue = ArrayDeque(listOf(5, 10, 15, 20))

    // Launch 4 coroutines to process values from the queue
    val jobs = List(4) { coroutineIndex ->
        launch {
            // Try to get a value from the queue
            val n = queue.removeFirstOrNull() ?: return@launch

            // Calculate factorial
            val result = calculateFactorial(n)
            println("Coroutine $coroutineIndex calculated factorial of $n = $result")
        }
    }

    // Wait for all coroutines to complete
    jobs.forEach { it.join() }
}

// Function to calculate factorial
suspend fun calculateFactorial(n: Int): Long {
    // Simulate some processing time
    delay(100 * n.toLong())

    var result = 1L
    for (i in 2..n) {
        result *= i
    }
    return result
}