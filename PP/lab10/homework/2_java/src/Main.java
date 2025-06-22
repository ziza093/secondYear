import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> numbers = List.of(5, 2, 9, 1, 7, 3);
        final int alpha = 3;

        // Create blocking queues for communication between stages
        BlockingQueue<List<Integer>> multiplyQueue = new LinkedBlockingQueue<>();
        BlockingQueue<List<Integer>> sortQueue = new LinkedBlockingQueue<>();

        // Thread 1: Multiply all elements by alpha
        Thread multiplyThread = new Thread(() -> {
            try {
                System.out.println("Stage 1: Multiplying all elements by " + alpha);
                List<Integer> multiplied = new ArrayList<>();
                for (Integer num : numbers) {
                    multiplied.add(num * alpha);
                }
                multiplyQueue.put(multiplied);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Thread 2: Sort the elements
        Thread sortThread = new Thread(() -> {
            try {
                List<Integer> multipliedNumbers = multiplyQueue.take();
                System.out.println("Stage 2: Sorting elements");
                Collections.sort(multipliedNumbers);
                sortQueue.put(multipliedNumbers);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Thread 3: Display the elements
        Thread displayThread = new Thread(() -> {
            try {
                List<Integer> sortedNumbers = sortQueue.take();
                System.out.println("Stage 3: Displaying elements");
                System.out.println("Final result: " + sortedNumbers);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Start all threads
        multiplyThread.start();
        sortThread.start();
        displayThread.start();

        // Wait for all threads to complete
        multiplyThread.join();
        sortThread.join();
        displayThread.join();
    }
}