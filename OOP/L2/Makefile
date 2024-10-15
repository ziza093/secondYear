main: main.o
	g++ main.o -Wall -o main

main.o: main.cpp
	g++ -Wall -c main.cpp

.PHONY: clean

clean:
	rm -f *.o
	rm main
