import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // List of files to process
        List<String> filePaths = Arrays.asList(
                "file1.txt",
                "file2.txt",
                "file3.txt",
                "file4.txt"
        );

        // Create sample files if they don't exist
        createSampleFiles(filePaths);

        // Create a queue for files to process
        BlockingQueue<String> fileQueue = new LinkedBlockingQueue<>(filePaths);

        // Create a thread pool of workers
        int workerCount = 3; // Number of concurrent workers
        Thread[] workers = new Thread[workerCount];
        CountDownLatch latch = new CountDownLatch(workerCount);

        // Start worker threads
        for (int i = 0; i < workerCount; i++) {
            final int workerIndex = i;
            workers[i] = new Thread(() -> {
                try {
                    String filePath;
                    // Process files until queue is empty
                    while ((filePath = fileQueue.poll()) != null) {
                        processFile(filePath, workerIndex);
                    }
                } finally {
                    latch.countDown();
                }
            });
            workers[i].start();
        }

        // Wait for all worker threads to complete
        latch.await();

        System.out.println("All files processed");
    }

    // Simulate file processing
    private static void processFile(String filePath, int workerIndex) {
        System.out.println("Worker " + workerIndex + " started processing " + filePath);

        try {
            // Read file content
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Simulate processing by counting words
            int wordCount = content.split("\\s+").length;

            // Simulate some heavy processing
            TimeUnit.MILLISECONDS.sleep(500);

            System.out.println("Worker " + workerIndex + " finished processing " + filePath + ": " + wordCount + " words");
        } catch (IOException | InterruptedException e) {
            System.err.println("Error processing file " + filePath + ": " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Helper function to create sample files for testing
    private static void createSampleFiles(List<String> filePaths) {
        try {
            for (int i = 0; i < filePaths.size(); i++) {
                String path = filePaths.get(i);
                String content = "This is a sample file " + i + " with some text content.\n" +
                        "It contains multiple lines for testing purposes.\n" +
                        "The file processing example will count words in this file.";
                Files.write(Paths.get(path), content.getBytes());
            }
        } catch (IOException e) {
            System.err.println("Error creating sample files: " + e.getMessage());
        }
    }
}