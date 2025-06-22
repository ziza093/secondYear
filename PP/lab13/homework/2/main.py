import more_itertools
from collections import defaultdict
from pprint import pprint

def map_function(word):
    if word:
        return word[0].lower(), word
    return '', word

def reduce_function(mapped_items):
    result = []
    for key, items in mapped_items:
        result.append((key, list(items)))
    return result

def word_sorting_map_reduce(text):
    words = [word.strip('.,!?:;()[]{}"\'-') for word in text.split()]
    
    mapped = [(key, value) for key, value in map(map_function, words)]
    
    grouped = defaultdict(list)
    for key, value in mapped:
        grouped[key].append(value)
    
    result = [(key, values) for key, values in grouped.items()]
    result.sort(key=lambda x: x[0])
    return result

if __name__ == "__main__":
    sample_text = "The functional paradigm in Python is strong, powerfull and elegant. The map, filter, and reduce functions are fundamental"
    
    result = word_sorting_map_reduce(sample_text)
    pprint(result)
    print("\n\n")


    key_func = lambda word: word[0].lower() if word else ''
    value_func = lambda word: word
    words = [word.strip('.,!?:;()[]{}"\'-') for word in sample_text.split()]
    
    result_with_itertools = more_itertools.map_reduce(words, key_func, value_func)
    pprint(dict(result_with_itertools))