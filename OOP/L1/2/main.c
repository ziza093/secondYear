#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "header.h"

int main(void){

    char *test = 0;
    int size = 0, pos = 0, n = 0;

    test = (char*)malloc(sizeof(char));

    printf("Read the word you want to know the size of: ");
    scanf("%s", test);

    size = str_length(test);

    printf("The length of the word is: %d\n", size);

    
    printf("Read the position from where you want to delete the characters:");
    scanf("%d", &pos);
    
    printf("How many characters you want to delete?");
    scanf("%d", &n);

    test = strdel(test, pos, n);

    printf("\n%s", test);

    memset(test, 0, sizeof(char));
    free(test);
    test = 0;

    return 0;
}
