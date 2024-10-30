#include "complex.h"

int main(){

    Complex a;
    a.citire();

    Complex b;
    b.citire();

    std::cout<<a.egal(b)<<std::endl;

    return 0;
}