#include "header.h"

int main(){

    Complex c1(4,5);
    Complex c2(2,3);
    Complex c3(2,3);
    Complex c4;

    c4.write();

    c4 = c2;
    c4.write();

    c4 = c1 + c2;
    c4.write();

    c4 = c1 - c2;
    c4.write();

    c4 = c1 * c2;
    c4.write();

    std::cout<<"The absolute value is: "<<~c4<<std::endl;

    if(c2 == c3){
        std::cout<<"The numbers are identical!\n"<<std::endl;
    }
    else{
        std::cout<<"The numbers are not identical!\n"<<std::endl;
    }


    return 0;
}