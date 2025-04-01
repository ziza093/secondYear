import os
import struct
import chardet

from xml.etree import ElementTree
from collections import Counter
from abc import ABC, abstractmethod
from pathlib import Path 




class GenericFile(ABC):
    @abstractmethod
    def get_path(self):
        raise NotImplementedError("get_path() is not implemented")
        
    def get_freq(self):
        raise NotImplementedError("get_freq() is not implemented")



class TextASCII(GenericFile):
    def __init__(self, path):
        self.path_absolute = path
        self.frequences = self.getFrequences()

    def getFrequences(self):

        try:
            f = open(self.path_absolute, "rb")
            content = f.read()
            freq = Counter(content)

        except Exception as e:
            print(f"Error while opening ASCII file: {e}")
            f.close()
            freq = {}

        return freq

    def get_path(self):
        return self.path_absolute

    def get_freq(self):
        return self.frequences



class TextUNICODE(GenericFile):
    def __init__(self, path):
        self.path_absolute = path
        self.frequences = self.getFrequences()

    def getFrequences(self):

        try:
            f = open(self.path_absolute, "r", encoding="utf-16")
            content = f.read()
            freq = Counter(content)

        except Exception as e:
            print(f"Error reading UNICODE file: {e}")
            freq = {}
            f.close()

        return freq
    
    def get_path(self):
        return self.path_absolute


    def get_freq(self):
        return self.frequences




class Binary(GenericFile):
    def __init__(self, path):
        self.path_absolute = path
        self.frequences = self.getFrequences()

    def getFrequences(self):

        try:
            f = open(self.path_absolute, "rb")
            content = f.read()
            freq = Counter(content)

        except Exception as e:
            print(f"Exception: {e}")
            freq = {}
            f.close()

        return freq
    
    def get_path(self):
        return self.path_absolute

    def get_freq(self):
        return self.frequences



class XMLFile(TextASCII):
    def __init__(self, path):
        super().__init__(path)
        self.first_tag = self.getFirst_tag()

    def getFirst_tag(self):

        try:
            tree = ElementTree.parse(self.path_absolute)
            tag = tree.getroot().tag

        except Exception as e:
            print(f"Exception: {e}")
            tag = None

        return tag
    
    def get_first_tag(self):
        return self.first_tag


class BMP(Binary):

    def __init__(self, path):
        super().__init__(path)
        self.width, self.height, self.bpp = self.getFrequences()

    def getFrequences(self):

        try:

            f = open(self.path_absolute, "rb")

            f.seek(18)  # Width and height are at byte 18 and 22
            width, height = struct.unpack('<ii', f.read(8))
            f.seek(28)  # Bits per pixel is at byte 28
            bpp = struct.unpack('<h', f.read(2))[0]
            return width, height, bpp

            print(content)

        except Exception as e:
            print(f"Exception: {e}")
            f.close()
            return None, None, None

    def show_info(self):
        print(f"The width is:{self.width}. The height is: {self.height}. The bpp is: {self.bpp}")



def getFiles():
    root_dir = os.path.dirname(os.path.abspath(__file__))
    path = input("Write the dir where the files are, but without the '/' at the beggining:")
    path = Path(path)
    path = Path(os.path.join(root_dir, path))

    if path.exists() and path.is_dir():
        for file in path.iterdir():
            if file.is_file():
                try:
                    f = open(file, "rb")
                    content = f.read()
                    
                    if isXML(content):
                        xml_file = XMLFile(file)
                        print(f"The file is an XML file and its path is {file}")
                    
                    elif isBMP(content):
                        bmp_file = BMP(file)
                        print(f"The file is an BMP file and its path is {file}")

                    elif isBinary(content):
                        binary_file = Binary(file)
                        print(f"The file is an Bonary file and its path is {file}")

                    
                    elif isUNICODE_UTF16(content):
                        unicode_file = TextUNICODE(file)
                        print(f"The file is an UNICODE file and its path is {file}")

                    elif isASCII_UTF8(content):
                        ascii_file = TextASCII(file)
                        print(f"The file is an ASCII file and its path is {file}")
                        



                finally:
                    f.close()


def isXML(content):
    try:
        text = content.decode(errors="ignore").strip()
        return text.startswith("<?xml") or ("<" in text and ">" in text)
    except Exception:
        return False

def isBMP(content):
    return len(content) > 2 and content[:2] == b'BM'


def isASCII_UTF8(content):
        char_counter = Counter(content)

        chars_Highfreq = [9,10,13,32] + list(range(33,128))
        chars_Lowfreq = list(range(9)) + [11,12,14,15] + list(range(16,32)) + list(range(128,256))

        high_freq_count = sum(char_counter[byte] for byte in chars_Highfreq if byte in char_counter)
        low_freq_count = sum(char_counter[byte] for byte in chars_Lowfreq if byte in char_counter)
        
        return (high_freq_count >= 0.7 * len(content)) and (low_freq_count <= 0.3 * len(content))


def isUNICODE_UTF16(content):
    
    #unicode files when read in binary mode they are written on 2 bytes, usually one is \x00
    null_count = content.count(b'\x00')
    null_percentage = null_count / len(content)


    return null_percentage >= 0.3

    
def isBinary(content):

    ascii_table = [chr(i) for i in range(256)]
    
    char_counter = Counter(content)

    for num in range(256):
        
        freq = char_counter[num]

        if not ((freq > 45/100 * len(content)) and 
                (freq < 55/100 * len(content))):
            return False


if __name__ == '__main__':
    getFiles()
    
