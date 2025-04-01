from tkinter import *
from tkinter import ttk


def addList(*args):
    numbers = list_integers.get().split(",")
    list = [str(x) for x in numbers] 
    result.set(list)


def filterOdd(*args):
    numbers = list_integers.get().split(",")
    list2 = [str(x) for x in numbers]
    list2 = [int(x) for x in list2]
    list2 = list(filter(lambda x: x % 2 == 0, list2)) 
    result.set(list2)


def primeNumber(n):
    if n == 1:
        return False

    for i in range(2,n):
        if(n%i == 0):
            return False
    
    return True
    

def filterPrimes(*args):
    numbers = list_integers.get().split(",")
    list2 = [str(x) for x in numbers]
    list2 = [int(x) for x in list2]
    list2 = list(filter(lambda x: primeNumber(x), list2)) 
    result.set(list2)

def sumNumbers(*args):
    numbers = list_integers.get().split(",")
    list2 = [str(x) for x in numbers]
    list2 = [int(x) for x in list2]
    list2 = sum(list2) 
    result.set(list2)


root = Tk()
root.title("Aplication 1 from laboratory")

mainframe = ttk.Frame(root, padding="20 20 40 40")
mainframe.grid(column=0, row=0, sticky=(N, W, E, S))
root.columnconfigure(0, weight=1)
root.rowconfigure(0, weight=1)

list_integers = StringVar()
list_integers_entry = ttk.Entry(mainframe, width=20, textvariable=list_integers)
list_integers_entry.grid(column=2, row=1, sticky=(W, E))

result = StringVar()
ttk.Label(mainframe, textvariable=result).grid(column=2, row=2, sticky=(W, E))

ttk.Button(mainframe, text="Add list", command=addList).grid(column=3, row=1, sticky=W)
ttk.Button(mainframe, text="Filter odd", command=filterOdd).grid(column=3, row=3, sticky=W)
ttk.Button(mainframe, text="Filter primes", command=filterPrimes).grid(column=3, row=4, sticky=W)
ttk.Button(mainframe, text="Sum numbers", command=sumNumbers).grid(column=3, row=5, sticky=W)


ttk.Label(mainframe, text="List of integers:").grid(column=1, row=1, sticky=W)
# ttk.Label(mainframe, text="is equivalent to").grid(column=1, row=2, sticky=E)
# ttk.Label(mainframe, text="meters").grid(column=3, row=2, sticky=W)

for child in mainframe.winfo_children(): 
    child.grid_configure(padx=5, pady=5)

list_integers_entry.focus()
root.bind("<Return>", addList)

root.mainloop()