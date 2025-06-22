import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun main() = runBlocking {
    // List of files to process
    val filePaths = listOf(
        "file1.txt",
        "file2.txt",
        "file3.txt",
        "file4.txt"
    )

    // Create sample files if they don't exist
    createSampleFiles(filePaths)

    // Create a channel for dispatching files to process
    val fileChannel = Channel<String>()

    // Producer coroutine - sends files to the channel
    launch {
        for (filePath in filePaths) {
            fileChannel.send(filePath)
        }
        fileChannel.close() // Close channel when all files are sent
    }

    // Create a pool of worker coroutines to process files
    val workerCount = 3 // Number of concurrent workers
    val jobs = List(workerCount) { workerIndex ->
        launch {
            // Each worker processes files from the channel
            for (filePath in fileChannel) {
                processFile(filePath, workerIndex)
            }
        }
    }

    // Wait for all workers to complete
    jobs.joinAll()

    println("All files processed")
}

// Simulate file processing
suspend fun processFile(filePath: String, workerIndex: Int) {
    println("Worker $workerIndex started processing $filePath")

    // Read file content
    val content = withContext(Dispatchers.IO) {
        File(filePath).readText()
    }

    // Simulate processing by counting words
    val wordCount = content.split("\\s+".toRegex()).size

    // Simulate some heavy processing
    delay(500)

    println("Worker $workerIndex finished processing $filePath: $wordCount words")
}

// Helper function to create sample files for testing
fun createSampleFiles(filePaths: List<String>) {
    for ((index, path) in filePaths.withIndex()) {
        val content = "This is a sample file $index with some text content.\n" +
                "It contains multiple lines for testing purposes.\n" +
                "The file processing example will count words in this file."
        Files.write(Paths.get(path), content.toByteArray())
    }
}