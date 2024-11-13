#include "header.h"

Complex::Complex(){
    this -> re = 0;
    this ->im = 0;
}

Complex::Complex(int re, int im){
    this->re = re;
    this->im = im;
}

void Complex::write(){
    std::cout<<"The complex number is: ";
    std::cout<<re;
    if(im < 0){
        std::cout<<im<<"i"<<std::endl;
    }
    else{
        std::cout<<"+"<<im<<"i"<<std::endl;
    }
}

Complex Complex::operator+(Complex c){

    Complex temp;

    temp.re = this->re + c.re;
    temp.im = this->im + c.im;

    return temp;
}

Complex Complex::operator-(Complex c){
    this->re = this->re - c.re;
    this->im = this->im - c.im;

    return *this;
}

Complex Complex::operator*(Complex c){
    
    Complex temp;

    temp.re = this->re * c.re;
    temp.im = -(this->im * c.im);

    return temp;
}

Complex Complex::operator=(Complex c){
    this->re = c.re;
    this->im = c.im;

    return *this;
}

bool Complex::operator==(Complex c){

    return this->re == c.re && this->im == c.im;
}

double Complex::operator~(){

    return sqrt(re*re + im*im);
}
