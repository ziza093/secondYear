#include "MultimeComplexe.h"

int main() {
 
    int choice;
    MultimeComplexe m(5);
    Complex c1(2,3), c2(3,4), c3(2,-1);
    Complex c4;

    do{
        std::cout<<"0. Exit\n";
        std::cout<<"1. Add element\n";
        std::cout<<"2. Remove element\n";

        std::cin>>choice;

        switch(choice){
            case 0:
                return 0;
                break;

            case 1:
                c4.citire();
                m.adauga(c4);
                m.afisare();
                break;
            
            case 2:
                c4.citire();
                m.extrage(c4);
                m.afisare();
                break;

            default: 
                std::cout<<"The number isn't in the menu!\n";
                break;
        }

    }while(choice);




    
 
    return 0;
}