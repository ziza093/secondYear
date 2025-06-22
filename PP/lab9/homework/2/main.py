from abc import ABC, abstractmethod
from enum import Enum

# Observer Pattern
class Observer(ABC):
    @abstractmethod
    def update(self, message):
        pass

class Observable:
    def __init__(self):
        self._observers = []
    
    def add_observer(self, observer):
        if observer not in self._observers:
            self._observers.append(observer)
    
    def remove_observer(self, observer):
        if observer in self._observers:
            self._observers.remove(observer)
    
    def notify_observers(self, message):
        for observer in self._observers:
            observer.update(message)

# State Pattern - Base Class
class State(ABC):
    @abstractmethod
    def handle(self):
        pass

# Product Enum
class Product(Enum):
    WATER = {"name": "Water", "price": 1.50}
    COLA = {"name": "Cola", "price": 2.00}
    JUICE = {"name": "Juice", "price": 2.50}
    SNACK = {"name": "Snack", "price": 3.00}

# Console Display Observer
class DisplayObserver(Observer):
    def update(self, message):
        print(f"DISPLAY: {message}")

# Main State Machines
class TakeMoneySTM(Observable):
    def __init__(self):
        super().__init__()
        self._current_amount = 0.0
    
    def insert_money(self, amount):
        self._current_amount += amount
        self.notify_observers(f"Current amount: ${self._current_amount:.2f}")
    
    def return_money(self):
        if self._current_amount > 0:
            return_amount = self._current_amount
            self._current_amount = 0.0
            self.notify_observers(f"Money returned: ${return_amount:.2f}")
            return return_amount
        return 0.0
    
    def get_current_amount(self):
        return self._current_amount
    
    def reduce_amount(self, amount):
        if amount <= self._current_amount:
            self._current_amount -= amount
            self.notify_observers(f"Remaining balance: ${self._current_amount:.2f}")
            return True
        return False

class SelectProductSTM:
    def __init__(self):
        self.selected_product = None
    
    def select_product(self, product):
        self.selected_product = product
        return self.selected_product
    
    def get_selected_product(self):
        return self.selected_product
    
    def reset(self):
        self.selected_product = None

# States for the main vending machine
class IdleState(State):
    def __init__(self, context):
        self.context = context
    
    def handle(self):
        self.context.display_observer.update("Insert money or select a product")
        return self  # Stay in idle state

class ProductSelectedState(State):
    def __init__(self, context):
        self.context = context
    
    def handle(self):
        product = self.context.select_product_stm.get_selected_product()
        
        if product is None:
            self.context.display_observer.update("No product selected. Returning to idle state.")
            return self.context.idle_state
        
        required_amount = product.value["price"]
        current_amount = self.context.take_money_stm.get_current_amount()
        
        if current_amount >= required_amount:
            # Sufficient funds
            self.context.display_observer.update(f"Dispensing {product.value['name']}...")
            self.context.take_money_stm.reduce_amount(required_amount)
            return self.context.dispense_state
        else:
            # Insufficient funds
            self.context.display_observer.update(
                f"Insufficient funds. Need ${required_amount:.2f}, you have ${current_amount:.2f}"
            )
            return self.context.idle_state

class DispenseState(State):
    def __init__(self, context):
        self.context = context
    
    def handle(self):
        product = self.context.select_product_stm.get_selected_product()
        self.context.display_observer.update(f"{product.value['name']} dispensed successfully!")
        self.context.select_product_stm.reset()
        
        # Ask if the user wants to continue shopping or get change
        current_amount = self.context.take_money_stm.get_current_amount()
        if current_amount > 0:
            self.context.display_observer.update(
                f"You have ${current_amount:.2f} remaining. Select another product or get change."
            )
            return self.context.idle_state
        else:
            self.context.display_observer.update("Thank you for your purchase!")
            return self.context.idle_state

# Central Entity
class VendingMachineSTM(Observer):
    def __init__(self):
        self.take_money_stm = TakeMoneySTM()
        self.select_product_stm = SelectProductSTM()
        self.display_observer = DisplayObserver()
        
        # Add observers
        self.take_money_stm.add_observer(self.display_observer)
        self.take_money_stm.add_observer(self)
        
        # Initialize states
        self.idle_state = IdleState(self)
        self.product_selected_state = ProductSelectedState(self)
        self.dispense_state = DispenseState(self)
        
        # Set initial state
        self.current_state = self.idle_state
    
    def update(self, message):
        # This will be called whenever the money amount changes
        pass  # We handle this in other parts of the code
    
    def insert_money(self, amount):
        self.take_money_stm.insert_money(amount)
    
    def select_product(self, product):
        if not isinstance(product, Product):
            self.display_observer.update(f"Invalid product selection")
            return
        
        selected = self.select_product_stm.select_product(product)
        if selected:
            self.display_observer.update(f"Selected: {selected.value['name']} - ${selected.value['price']:.2f}")
            self.current_state = self.product_selected_state
            self.process_state()
    
    def return_money(self):
        returned = self.take_money_stm.return_money()
        if returned > 0:
            self.display_observer.update(f"Please take your ${returned:.2f}")
    
    def process_state(self):
        # Process the current state and get the next state
        next_state = self.current_state.handle()
        if next_state != self.current_state:
            self.current_state = next_state
    
    def run(self):
        self.display_observer.update("Welcome to the Vending Machine!")
        self.display_observer.update("Available products:")
        for product in Product:
            self.display_observer.update(f"- {product.value['name']}: ${product.value['price']:.2f}")
        
        while True:
            print("\n" + "-" * 30)
            print("1. Insert money")
            print("2. Select product")
            print("3. Return money")
            print("4. Exit")
            
            choice = input("Enter your choice (1-4): ")
            
            if choice == '1':
                try:
                    amount = float(input("Enter amount to insert: $"))
                    if amount > 0:
                        self.insert_money(amount)
                    else:
                        print("Please enter a positive amount.")
                except ValueError:
                    print("Invalid amount. Please enter a number.")
            
            elif choice == '2':
                print("\nAvailable products:")
                for i, product in enumerate(Product, 1):
                    print(f"{i}. {product.value['name']}: ${product.value['price']:.2f}")
                
                try:
                    product_choice = int(input("Select a product (1-4): "))
                    if 1 <= product_choice <= 4:
                        self.select_product(list(Product)[product_choice - 1])
                    else:
                        print("Invalid product selection.")
                except ValueError:
                    print("Invalid input. Please enter a number.")
            
            elif choice == '3':
                self.return_money()
            
            elif choice == '4':
                print("Thank you for using the Vending Machine!")
                break
            
            else:
                print("Invalid choice. Please try again.")


if __name__ == "__main__":
    vending_machine = VendingMachineSTM()
    vending_machine.run()