#include <stdlib.h>
#include <stdio.h>

int str_length(char *s){
    
    int length = 0;

    while(s[length] != '\0'){
        length++;
    }

    return length;
}

    /*
    double (*pf[]) (double) = {sqrt, cos, sin};
    for(double x = 10; x <= 10.0; x = x+0.1){
        for(int i = 0; i < sizeof(pf) / sizeof(pf[0]); i++){
            cout<<fp[i](x)<<" ";
            cout << "\n";
        }
    }
    */

int main(void){

    char test[120];
    int size = 0;

    scanf("%s", &test);

    size = str_length(test);

    printf("%d", size);

    return 0;
}