#include <iostream>
#include "header.h"

int main(){

    Stiva s;

    s.push(4);
    s.push(3);
    std::cout <<"The top value is: "<< s.top() <<std::endl;
    s.push(9);
    std::cout <<"You removed the value: "<< s.pop() <<std::endl;
    s.push(2);
    s.print();

    return 0;
}