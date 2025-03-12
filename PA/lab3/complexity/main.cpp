#include "header.h"

int main(void){

    int a[100] = {};
    int b[100] = {};
    int c[100] = {};
    float complexity[3] = {};
    int size = 0;

    std::cout<<"Citesete marimea vectorului: \n";
    std::cin>>size;


    //3 58 12 124 -1 4 -421 431 341 2381 9 0 -1 -2 3242 -312 3123 543 9 -13245
    citire(a, size);
    copiere(a, b, size);
    copiere(a, c, size);

    auto start = std::chrono::high_resolution_clock::now();     //Start timer
    mergeSort(a, 0, size-1);
    auto end = std::chrono::high_resolution_clock::now();       //End timer

    auto duration = (end - start);                              //Calc duration
    auto us = std::chrono::duration_cast<std::chrono::microseconds>(duration);      
    auto ms = std::chrono::duration_cast<std::chrono::milliseconds>(duration);
    const float ms_fractional = static_cast<float>(us.count()) / 1000;             

    complexity[0] = ms_fractional;

    std::cout << "Duration = " << us.count() << "µs (" << ms_fractional << "ms)" << std::endl;

    std::cout<<"Sortat: ";
    afisare(a, size);



    start = std::chrono::high_resolution_clock::now();     //Start timer
    quickSort1(b, 0, size-1);
    end = std::chrono::high_resolution_clock::now();       //End timer

    duration = (end - start);                              //Calc duration
    us = std::chrono::duration_cast<std::chrono::microseconds>(duration);      
    ms = std::chrono::duration_cast<std::chrono::milliseconds>(duration);
    const float ms_fractional2 = static_cast<float>(us.count()) / 1000;     
    
    complexity[1] = ms_fractional2;    

    std::cout << "Duration = " << us.count() << "µs (" << ms_fractional2 << "ms)" << std::endl;

    std::cout<<"Sortat: ";
    afisare(b, size);
    
    start = std::chrono::high_resolution_clock::now();     //Start timer
    quickSort2(c, 0, size-1);
    end = std::chrono::high_resolution_clock::now();       //End timer

    duration = (end - start);                              //Calc duration
    us = std::chrono::duration_cast<std::chrono::microseconds>(duration);      
    ms = std::chrono::duration_cast<std::chrono::milliseconds>(duration);
    const float ms_fractional3 = static_cast<float>(us.count()) / 1000;     
    
    complexity[2] = ms_fractional3;

    std::cout << "Duration = " << us.count() << "µs (" << ms_fractional3 << "ms)" << std::endl;


    std::cout<<"Sortat: ";
    afisare(c, size);

    std::cout << "MergeSort duration = " << us.count() << "µs (" << complexity[0] << "ms)" << std::endl;
    std::cout << "QuickSort1 duration = " << us.count() << "µs (" << complexity[1] << "ms)" << std::endl;
    std::cout << "QuickSort2 duration = " << us.count() << "µs (" << complexity[2] << "ms)" << std::endl;


    return 0;
}