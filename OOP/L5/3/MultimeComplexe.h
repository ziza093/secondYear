#pragma once
#include <iostream>

class Complex{

    int re,im;
    
    public:
        int egal(Complex c2);
        void citire();
        void afisareC();
        Complex();
        Complex(int,int);
};

class MultimeComplexe{

    Complex *v;
    int dim;
    int n;

    public:
        void adauga(Complex);
        void extrage(Complex);
        void afisare();
        MultimeComplexe(int);
};