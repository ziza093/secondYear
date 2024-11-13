#include "header.h"

int main() {

    Multimea m1(3);
    Multimea m2(5);
    Multimea m3(3);


    m2 += 12;
    m2 += 3;
    m2 -= 12;
    m2 += 1;

    m1 = m2;
    std::cout<<"Write m1: ";
    m1.write();

    m1 += m2;
    std::cout<<"Write m1: ";
    m1.write();

    m3 = m1 + m1;
    std::cout<<"Write m3: ";
    m3.write();

    return 0;
}