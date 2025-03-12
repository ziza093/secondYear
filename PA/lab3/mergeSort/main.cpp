#include "mergeSort.h"

int main(void){

    int a[100] = {};
    int size;

    std::cout<<"Citeste dimensiune vector!\n";
    std::cin>>size;

    citireVector(a, size);
    afisareVector(a, size);

    mergeSort(a, 0, size-1);
    afisareVector(a, size);

    return 0;
}