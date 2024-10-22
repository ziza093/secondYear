min: main.o functions.o header.h
	gcc main.o functions.o -o main

main.o: main.c header.h
	gcc -c main.c -Wall

functions.o: functions.c header.h
	gcc -c functions.c -Wall

.PHONY: clean
	
clean: 
	rm -f *.o
	rm main
