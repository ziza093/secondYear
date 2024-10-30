#include "complex.h"

int Complex::egal(Complex c2){
    return this->im == c2.im && this->re == c2.re;
}

void Complex::citire(){
    std::cout<<"Read the real part:";
    std::cin>>this->re;

    std::cout<<"Read the imaginary part:";
    std::cin>>this->im;
}