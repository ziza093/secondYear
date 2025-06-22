import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Queue with different values of n
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(5);
        queue.add(10);
        queue.add(15);
        queue.add(20);

        // Create threads and synchronization mechanism
        Thread[] threads = new Thread[4];
        CountDownLatch latch = new CountDownLatch(4);

        // Create and start 4 threads
        for (int i = 0; i < 4; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                try {
                    // Get a value from the queue (synchronized to prevent race conditions)
                    Integer n;
                    synchronized (queue) {
                        n = queue.poll();
                    }

                    if (n != null) {
                        // Calculate factorial
                        long result = calculateFactorial(n);
                        System.out.println("Thread " + threadIndex + " calculated factorial of " + n + " = " + result);
                    }
                } finally {
                    latch.countDown();
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        latch.await();
    }

    // Function to calculate factorial
    private static long calculateFactorial(int n) {
        try {
            // Simulate some processing time
            Thread.sleep(100 * n);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long result = 1L;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}