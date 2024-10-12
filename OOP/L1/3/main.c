#include <math.h>
#include <stdio.h>

void listf (double (*fp)(double), double min, double max, double pas) {
    
    double x,y;

    for (x = min; x <= max; x = x + pas) {
        y=(*fp)(x);
        printf("\n%20.10lf %20.10lf", x, y);
    }
}

int main(void){

   double (*pf[6]) (double) = {sqrt, sin, cos, tan, exp, log};

    for(int i = 0; i < 6; i++){
        listf(pf[i], 1, 10, 0.1);
    }

    return 0;
}