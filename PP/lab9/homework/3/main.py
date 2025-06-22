import requests
import json
import time
import os
import multiprocessing
from abc import ABC, abstractmethod
from datetime import datetime, timedelta

# Proxy Pattern
class HTTPClient(ABC):
    @abstractmethod
    def get(self, url):
        pass

class RealHTTPClient(HTTPClient):
    def get(self, url):
        response = requests.get(url)
        return {
            'status_code': response.status_code,
            'content': response.text,
            'headers': dict(response.headers)
        }

class CachingProxyClient(HTTPClient):
    def __init__(self, cache_file="cache.json", expiration_time=3600):
        self.real_client = RealHTTPClient()
        self.cache_file = cache_file
        self.expiration_time = expiration_time  # Cache expiration in seconds (1 hour)
        self.load_cache()
        
        # For request monitoring (bonus)
        self.request_counter = 0
        self.last_count_time = time.time()
        self.request_threshold = 10  # Threshold for load balancing
        self.time_window = 60  # Time window in seconds (1 minute)
        
        # Load balancing strategy (initially None, will be set based on load)
        self.load_balancing_strategy = None
    
    def load_cache(self):
        try:
            if os.path.exists(self.cache_file):
                with open(self.cache_file, 'r') as f:
                    self.cache = json.load(f)
            else:
                self.cache = {}
        except (json.JSONDecodeError, FileNotFoundError):
            self.cache = {}
    
    def save_cache(self):
        with open(self.cache_file, 'w') as f:
            json.dump(self.cache, f, indent=2)
    
    def get(self, url):
        # Monitor request rate for load balancing
        self.monitor_request_rate()
        
        # If load balancing is active, use the strategy
        if self.load_balancing_strategy:
            return self.load_balancing_strategy.handle_request(url, self)
        
        # Otherwise, process the request normally
        return self._process_request(url)
    
    def _process_request(self, url):
        current_time = time.time()
        
        # Check if URL is in cache and not expired
        if url in self.cache:
            timestamp = self.cache[url]['timestamp']
            if current_time - timestamp < self.expiration_time:
                print(f"Cache hit for {url}")
                return self.cache[url]['response']
        
        # Cache miss or expired - get from real client
        print(f"Cache miss for {url}, fetching fresh data")
        response = self.real_client.get(url)
        
        # Update cache
        self.cache[url] = {
            'timestamp': current_time,
            'response': response
        }
        
        # Save updated cache to file
        self.save_cache()
        
        return response
    
    def monitor_request_rate(self):
        # Count this request
        self.request_counter += 1
        current_time = time.time()
        
        # Check if we're still in the time window
        if current_time - self.last_count_time > self.time_window:
            # Reset counter for new time window
            self.request_counter = 1
            self.last_count_time = current_time
            
            # If we had a load balancing strategy, reset it if load is low
            if self.load_balancing_strategy:
                print("Load decreased, disabling load balancing")
                self.load_balancing_strategy = None
        
        # Check if request rate exceeded threshold within the time window
        elif self.request_counter >= self.request_threshold and not self.load_balancing_strategy:
            print(f"High request rate detected ({self.request_counter} in {self.time_window}s). Activating load balancing.")
            self.load_balancing_strategy = RoundRobinStrategy()

# Strategy Pattern for Load Balancing (BONUS)
class LoadBalancingStrategy(ABC):
    @abstractmethod
    def handle_request(self, url, proxy):
        pass

class RoundRobinStrategy(LoadBalancingStrategy):
    def __init__(self):
        self.process_count = 2  # Number of processes to use
        self.current_process = 0
        self._create_process_pool()
    
    def _create_process_pool(self):
        # Create a shared manager to hold our results
        self.manager = multiprocessing.Manager()
        self.results = self.manager.dict()
        self.processes = []
    
    def handle_request(self, url, proxy):
        # Simple round-robin selection
        self.current_process = (self.current_process + 1) % self.process_count
        
        # Create a process to handle this request
        process = multiprocessing.Process(
            target=self._process_request,
            args=(url, proxy, self.results, f"process_{self.current_process}")
        )
        
        # Start the process
        process.start()
        
        # Wait for the process to finish and get the result
        process.join()
        
        # Return the result
        if f"process_{self.current_process}" in self.results:
            return self.results[f"process_{self.current_process}"]
        else:
            # Fallback to direct processing if something went wrong
            return proxy._process_request(url)
    
    @staticmethod
    def _process_request(url, proxy, results, process_id):
        # Process the request in a separate process
        response = proxy._process_request(url)
        results[process_id] = response

def main():
    caching_client = CachingProxyClient()
    
    while True:
        print("\n" + "-" * 30)
        print("HTTP Request Caching Proxy")
        print("1. Make a GET request")
        print("2. View cache status")
        print("3. Clear cache")
        print("4. Exit")
        
        choice = input("Enter your choice (1-4): ")
        
        if choice == '1':
            url = input("Enter URL to fetch: ")
            try:
                start_time = time.time()
                response = caching_client.get(url)
                end_time = time.time()
                
                print(f"Request completed in {end_time - start_time:.2f} seconds")
                print(f"Status code: {response['status_code']}")
                print(f"Content length: {len(response['content'])} characters")
                
                # Show a snippet of the content
                content_preview = response['content'][:150] + "..." if len(response['content']) > 150 else response['content']
                print(f"Content preview: {content_preview}")
                
            except Exception as e:
                print(f"Error making request: {e}")
        
        elif choice == '2':
            caching_client.load_cache()  # Refresh cache from file
            if not caching_client.cache:
                print("Cache is empty")
            else:
                print(f"Cache contains {len(caching_client.cache)} URLs:")
                for url, data in caching_client.cache.items():
                    timestamp = data['timestamp']
                    cache_time = datetime.fromtimestamp(timestamp)
                    expiry_time = cache_time + timedelta(seconds=caching_client.expiration_time)
                    now = datetime.now()
                    
                    status = "Valid" if now < expiry_time else "Expired"
                    time_left = (expiry_time - now).total_seconds() if now < expiry_time else 0
                    
                    print(f"- {url} ({status}, expires in {time_left:.0f} seconds)")
        
        elif choice == '3':
            caching_client.cache = {}
            caching_client.save_cache()
            print("Cache cleared")
        
        elif choice == '4':
            print("Exiting application")
            break
        
        else:
            print("Invalid choice. Please try again.")

if __name__ == "__main__":
    main()