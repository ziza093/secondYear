import functools
import operator
from itertools import zip_longest

def process_list_functional(input_list):
    filtered_list = list(filter(lambda x: x >= 5, input_list))
    print(f"After eliminating < 5 numbers: {filtered_list}")
    
    grouped_list = list(zip(filtered_list[0::2], filtered_list[1::2]))
    print(f"After grouping in pairs: {grouped_list}")
    
    multiplied_pairs = list(map(lambda pair: operator.mul(*pair), grouped_list))
    print(f"After multiplying pairs: {multiplied_pairs}")
    
    sum_result = functools.reduce(operator.add, multiplied_pairs, 0)
    print(f"Final sum: {sum_result}")
    
    return sum_result


if __name__ == "__main__":
    numbers = [1, 21, 75, 39, 7, 2, 35, 3, 31, 7, 8]
    
    result1 = process_list_functional(numbers)
    