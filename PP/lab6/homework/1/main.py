import os
from pathlib import Path
from collections import Counter


class FileTypes:

    def __init__(self):

        root_dir = os.path.dirname(os.path.abspath(__file__))
        self.path = input("Write the dir where the files are, but without the '/' at the beggining:")
        self.path = Path(self.path)
        self.path = Path(os.path.join(root_dir, self.path))

        self.chars_Highfreq = [9,10,13,32] + list(range(33,128))
        self.chars_Lowfreq = list(range(9)) + [11,12,14,15] + list(range(16,32)) + list(range(128,256))

        char_counter = Counter()

        if self.path.exists() and self.path.is_dir():
            for file in self.path.iterdir():
                if file.is_file():
                    try:
                        f = open(file, "rb")
                        content = f.read()
                        # char_counter.update(Counter(content))
                        # char_dict = {char: char_counter[char] for char in ascii_table}
                        

                        #test the files
                        if self.isASCII_UTF8(content):
                            print(f"{file} is ASCII/UTF8")
                        
                        elif self.isBinary(content):
                            print(f"{file} is Binary")
                        
                        elif self.isUNICODE_UTF16(content):
                            print(f"{file} is UNICODE/UTF16")
                        
                    finally:
                        f.close()



    def isASCII_UTF8(self, content):
        char_counter = Counter(content)

        high_freq_count = sum(char_counter[byte] for byte in self.chars_Highfreq if byte in char_counter)
        low_freq_count = sum(char_counter[byte] for byte in self.chars_Lowfreq if byte in char_counter)
        
        return (high_freq_count >= 0.7 * len(content)) and (low_freq_count <= 0.3 * len(content))


    def isUNICODE_UTF16(self, content):
        
        #unicode files when read in binary mode they are written on 2 bytes, usually one is \x00
        null_count = content.count(b'\x00')
        null_percentage = null_count / len(content)
    

        return null_percentage >= 0.3

       
    def isBinary(self, content):

        ascii_table = [chr(i) for i in range(256)]
        
        char_counter = Counter(content)
        # char_dictionary = {char: char_counter[ord(char)] for char in ascii_table}

        print(content)
        print(char_counter)

        for num in range(256):
            
            freq = char_counter[num]

            if not ((freq > 45/100 * len(content)) and 
                    (freq < 55/100 * len(content))):
                return False


if __name__ == '__main__':
    file = FileTypes()