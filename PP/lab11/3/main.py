import threading
import queue
import time
from typing import List, Callable, TypeVar, Generic, Iterable, Iterator

T = TypeVar('T')  # Input elements type
R = TypeVar('R')  # Output elements type

class CustomThreadPool:
    """
    Custom ThreadPool implementation which offers:
    - Usage inside a with block (context manager)
    - Map function with automated balance of work volume
    - Join and Terminate methods
    """
    
    def __init__(self, num_threads=None):
        """
        Initialize the pool with a specific number of threads
        If num_threads is None, will use the number of cores available
        """
        self.num_threads = num_threads or threading.active_count()
        self.task_queue = queue.Queue()
        self.workers = []
        self.results_queue = queue.Queue()
        self.is_running = False
        self.is_joining = False
    
    def __enter__(self):
        """Method to use the pool inside a with block"""
        self.start()
        return self
    
    def __exit__(self, exc_type, exc_val, exc_tb):
        """It gets called automatically when exiting the 'with' block"""
        self.join()
        self.terminate()
    
    def _worker_thread(self):
        """The function run by every thread in the pool"""
        while self.is_running:
            try:
                # Takes a task from the queue with a timeout
                task, args, kwargs = self.task_queue.get(timeout=0.1)
                
                try:
                    # Executes the task and stores the result in the results queue
                    result = task(*args, **kwargs)
                    self.results_queue.put((True, result))
                except Exception as e:
                    # In case of errors, stores the exception in the results queue
                    self.results_queue.put((False, e))
                
                # Marks the task as done
                self.task_queue.task_done()
                
            except queue.Empty:
                # The queue is empty temporarily
                if self.is_joining:
                    # When joining, the process can stop
                    break
    
    def start(self):
        """Start the threads"""
        if not self.is_running:
            self.is_running = True
            self.workers = []
            
            for i in range(self.num_threads):
                worker = threading.Thread(
                    target=self._worker_thread,
                    name=f"ThreadPool-Worker-{i}"
                )
                worker.daemon = True
                worker.start()
                self.workers.append(worker)
    
    def submit(self, fn, *args, **kwargs):
        """Add a task to the task queue"""
        if not self.is_running:
            raise RuntimeError("ThreadPool hasn't started")
        
        self.task_queue.put((fn, args, kwargs))
    
    def map(self, fn: Callable[[T], R], iterable: Iterable[T]) -> List[R]:
        """
        Applies the function fn to each element in the iterable and returns the results.
        Automatically distributes the workload between threads.
        """
        if not self.is_running:
            self.start()
        
        # Convert the iterable to a list to calculate its length
        items = list(iterable)
        num_items = len(items)
        
        if num_items == 0:
            return []
        
        # Calculate how to distribute elements among threads
        base_items_per_thread = num_items // self.num_threads
        extra_items = num_items % self.num_threads
        
        results = [None] * num_items
        index = 0
        
        # Create a function to process a chunk of elements
        def process_chunk(start_idx, end_idx, chunk_items):
            chunk_results = []
            for item in chunk_items:
                chunk_results.append(fn(item))
            return start_idx, chunk_results
        
        # Distribute the elements between threads
        for i in range(self.num_threads):
            # Calculate how many elements this thread will receive
            items_for_this_thread = base_items_per_thread
            if i < extra_items:
                items_for_this_thread += 1
            
            if items_for_this_thread > 0:
                end_index = index + items_for_this_thread
                chunk = items[index:end_index]
                self.submit(process_chunk, index, end_index, chunk)
                index = end_index
        
        # Collect the results
        collected_results = 0
        while collected_results < self.num_threads and collected_results < num_items:
            success, result = self.results_queue.get()
            
            if success:
                start_idx, chunk_results = result
                for i, res in enumerate(chunk_results):
                    results[start_idx + i] = res
            else:
                # Propagate the exception if one occurred
                raise result
            
            collected_results += 1
        
        return results
    
    def join(self):
        """Wait for all tasks in the queue to be finished"""
        if not self.is_running:
            return
        
        self.is_joining = True
        self.task_queue.join()
        
        # Wait for all threads to finish
        for worker in self.workers:
            if worker.is_alive():
                worker.join()
    
    def terminate(self):
        """Stop all threads in the pool, even if there are tasks left in the queue"""
        self.is_running = False
        
        # Empty the task queue
        while not self.task_queue.empty():
            try:
                self.task_queue.get_nowait()
                self.task_queue.task_done()
            except queue.Empty:
                break
        
        # Mark as joining to allow threads to stop
        self.is_joining = True
        
        # Wait for all threads to finish
        for worker in self.workers:
            if worker.is_alive():
                worker.join(timeout=0.1)
        
        self.workers = []


# Example of usage
def test_custom_thread_pool():
    print("Testing CustomThreadPool implementation...")
    
    # Test function that takes some time to simulate parallelism
    def square_with_delay(x):
        time.sleep(0.1)  # Simulate an operation that takes time
        return x * x
    
    # Test usage in a with block
    print("\nTesting usage with 'with':")
    with CustomThreadPool(num_threads=4) as pool:
        # Test map with 9 elements
        data = list(range(1, 10))
        print(f"Applying map to {data}")
        
        start_time = time.time()
        results = pool.map(square_with_delay, data)
        end_time = time.time()
        
        print(f"Results: {results}")
        print(f"Duration: {end_time - start_time:.2f} seconds")
    
    # Test explicit usage (without 'with')
    print("\nTesting explicit usage (without 'with'):")
    pool = CustomThreadPool(num_threads=4)
    pool.start()
    
    data = list(range(10, 20))
    print(f"Applying map to {data}")
    
    start_time = time.time()
    results = pool.map(square_with_delay, data)
    end_time = time.time()
    
    print(f"Results: {results}")
    print(f"Duration: {end_time - start_time:.2f} seconds")
    
    pool.join()
    pool.terminate()
    
    # Test for load balancing verification
    print("\nTesting load balancing:")
    print("With a single thread it should take around 1.3 seconds for 13 elements")
    print("With 4 threads and correct load balancing, it should take around 0.4 seconds")
    
    with CustomThreadPool(num_threads=4) as pool:
        data = list(range(1, 14))  # 13 elements
        
        start_time = time.time()
        results = pool.map(square_with_delay, data)
        end_time = time.time()
        
        print(f"Results: {results}")
        print(f"Duration: {end_time - start_time:.2f} seconds")
        
        # The ideal distribution would be: 4 + 3 + 3 + 3 elements for the 4 threads
        # So it should take approximately: 4 * 0.1 = 0.4 seconds

if __name__ == "__main__":
    test_custom_thread_pool()
