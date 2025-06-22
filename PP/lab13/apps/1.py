class Int(int):
    def is_prime(self):
        if self < 2:
            return False
        for i in range(2, int(self ** 0.5) + 1):
            if self % i == 0:
                return False
        return True


if __name__ == "__main__":
    print(Int(7).is_prime()) 
    print(Int(10).is_prime())  
