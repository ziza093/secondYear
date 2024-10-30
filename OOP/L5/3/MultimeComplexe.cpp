#include "MultimeComplexe.h"

void Complex::afisareC(){
    std::cout<<this->re;
    if(this->im > 0)
        std::cout<<"+"<<this->im<<"i\n";
    else{
        if(this->im == 0){
            std::cout<<"+"<<0<<std::endl;
        }
        else{
            std::cout<<this->im<<"i\n";
        }
    }
}

Complex::Complex(){
    this->im = 0;
    this->re = 0;
}

Complex::Complex(int re, int im)
{
    this->re = re;
    this->im = im;
}

int Complex::egal(Complex c2){
    return this->im == c2.im && this->re == c2.re;
}

void Complex::citire(){
    std::cout<<"Read the real part:";
    std::cin>>this->re;

    std::cout<<"Read the imaginary part:";
    std::cin>>this->im;
}

void MultimeComplexe::adauga(Complex c){
    for(int i=0; i<n; i++){
        if(this->v[i].egal(c)){
            return;
        }
    }

    this->v[n++] = c;
}

void MultimeComplexe::extrage(Complex c){
    for(int i=0; i<n; i++){
        if(this->v[i].egal(c)){
            for(int j=i; j<n-1; j++){
                this->v[j] = this->v[j+1];
            }

            n--;
        }
    }
    
}

void MultimeComplexe::afisare(){
    for(int i=0; i<n; i++){
        v[i].afisareC();
    }
}

MultimeComplexe::MultimeComplexe(int dim){
    this->v = new Complex[dim];
}