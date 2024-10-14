main: main.o
	gcc main.o -Wall -lm -o main

main.o: main.c
	gcc -Wall -c main.c

.PHONY: clean

clean:
	rm -f *.o
	rm main
