main: main.o functions.o catalog.h
	g++ -Wall main.o functions.o -o main

functions.o: functions.cpp catalog.h
	g++ -Wall -c functions.cpp

main.o: main.cpp catalog.h
	g++ -Wall -c main.cpp



.PHONY: clean
	clean: 
		rm -f *.o
		rm main
