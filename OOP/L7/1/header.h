//supraincarcare operator +
#include <iostream>
#include <math.h>

class Complex {

    int re,im;
    
    public:
        Complex ();
        Complex (int,int);
        void write();
        Complex operator+(Complex);
        Complex operator-(Complex);
        Complex operator*(Complex);
        Complex operator=(Complex);
        bool operator==(Complex);
        double operator~();
};