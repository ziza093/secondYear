#include <stdio.h>

int main(void){

    int n;
    char test;  

    scanf("%d", &n);

    test =  *((char*)&n);

    if(test){
        printf("Big-endian");
        printf(" , %d\n", test);
    }
    else{
        printf("Little-endian");
        printf(" , %d\n", test);
    }

    return 0;
}