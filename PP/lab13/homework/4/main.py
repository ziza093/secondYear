from functools import reduce

class Automat:
    def __init__(self, numbers):
        self.numbers = numbers
        self.current_state = "state - 0"
        
        self.is_prime = lambda n: n > 1 and all(n % i != 0 for i in range(2, int(n**0.5) + 1))
        self.is_not_prime = lambda n: not self.is_prime(n)
        self.is_even = lambda n: n % 2 == 0
        self.is_greater_than_50 = lambda n: n > 50
        
        self.state_transitions = {
            "state - 0": (
                lambda lst: any(map(self.is_not_prime, lst)),  
                lambda lst: list(filter(self.is_prime, lst)),  
                "state - 1"
            ),
            "state - 1": (
                lambda lst: any(map(self.is_even, lst)), 
                lambda lst: list(filter(lambda x: not self.is_even(x), lst)), 
                "state - 2" 
            ),
            "state - 2": (
                lambda lst: any(map(self.is_greater_than_50, lst)),  
                lambda lst: list(filter(lambda x: not self.is_greater_than_50(x), lst)),
                "STOP"  
            ),
            "STOP": (
                lambda lst: False, 
                lambda lst: lst,  
                "STOP" 
            )
        }
    
    def run(self):
        print(f"Start in {self.current_state} with the list: {self.numbers}")
        
        while self.current_state != "STOP" or self.state_transitions[self.current_state][0](self.numbers):
            current_state_data = self.state_transitions[self.current_state]
            
            if current_state_data[0](self.numbers):
                self.numbers = current_state_data[1](self.numbers)
                print(f"Stay in {self.current_state}, the list becomes: {self.numbers}")
            else:
                next_state = current_state_data[2]
                print(f"From {self.current_state} to {next_state}")
                self.current_state = next_state
        
        print(f"Finally {self.current_state} with the list: {self.numbers}")
        return self.numbers


if __name__ == "__main__":
    test_numbers = [4, 6, 8, 7, 11, 13, 16, 20, 37, 42, 53, 67, 89, 91, 17, 23, 29]
    
    automat = Automat(test_numbers.copy())
    result1 = automat.run()