#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "header.h"

int main(void){

    char *test = 0, *s1 = 0, *s2 = 0;
    int size = 0, pos = 0, n = 0, choice = 0;

    test = (char*)malloc(sizeof(char));
    s1 = (char*)malloc(sizeof(char));
    s2 = (char*)malloc(sizeof(char));
        
    do{

        printf("Choose something from the menu:\n");
        printf("0. Exit\n");
        printf("1. a)\n");
        printf("2. b)\n");
        printf("3. c)\n");
        printf("4. d)\n");
        printf("5. e)\n");

        scanf("%d", &choice);

        switch(choice){

            case 1:
                printf("Read the word you want to know the size of: ");
                scanf("%s", test);

                size = str_length(test);

                printf("The length of the word is: %d\n", size);
                
                break;

            case 2:
                printf("Read the position from where you want to delete the characters:");
                scanf("%d", &pos);
    
                printf("How many characters you want to delete?");
                scanf("%d", &n);

                test = strdel(test, pos, n);

                printf("\n%s\n", test);
                
                break;

            case 3:
                printf("Read two strings s1 and s2:"); 
                scanf("%s", s1);
                scanf("%s", s2);
                
                printf("Read the position from where you want to insert s2 in s1: ");
                scanf("%d", &pos);
                
                test = strins(s1, pos, s2);

                printf("\n%s\n", test);
                
                break;

            case 4:
                printf("Read two strings sir and mask. Mask may contain the ? char which has the role of an joker, it can substitute any character\n");
                scanf("%s", s1);
                scanf("%s", s2);
                
                int check = eq_mask(s1, s2);

                if(check){
                    printf("The strings are identical\n");
                }
                else{
                    printf("The strings are not identical\n");
                }

                break;

            case 5:
	              char *table[100] = {"course1", "course2", "course3", "course4", "course5"};
	              char *cuv1 = "course2", *cuv2 = "course7";
	              printf("course2 %s in table\n",(eqcuv(cuv1, table))?"is":"isn't");
	              printf("course7 %s in table\n",(eqcuv(cuv2, table))?"is":"isn't");
                
                break;
        }

    }while(choice);

    memset(test, 0, sizeof(char));
    free(test);
    test = 0;

    memset(s1, 0, sizeof(char));
    free(s1);
    s1 = 0;
    
    memset(s2, 0, sizeof(char));
    free(s2);
    s2 = 0;
    
    return 0;
}
