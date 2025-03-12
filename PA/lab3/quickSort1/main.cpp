#include "quickSort1.h"

int main(void){

    int a[100] = {};
    int size;

    std::cout<<"Citeste cate elem sa aiba vectorul: ";
    std::cin>>size;

    citire(a, size);
    afisare(a, size);

    quickSort1(a, 0, size-1);
    afisare(a, size);




    return 0;
}