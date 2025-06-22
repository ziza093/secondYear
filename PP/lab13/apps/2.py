class PascalStr(str):
    def to_pascal_case(self):
        return ''.join(word.capitalize() for word in self.split())


if __name__ == '__main__':
    print(PascalStr("convert this string").to_pascal_case())  
