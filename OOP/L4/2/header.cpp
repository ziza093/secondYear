#include <iostream>
#include "header.h"

Stiva::Stiva(){
    dim = 20;
    n = 0;
    date = new int[dim];
}

void Stiva::push(int value){
    if(n==dim){
        std::cout<<"The stack is full!\n";
        return;
    }

    date[n++] = value;
}

int Stiva::pop(){
    if(n == -1){
        std::cout<<"The stack is empty!\n";
        return -1;
    }   
    
    n--;
    
    return date[n];
}

int Stiva::top(){
    if(n != -1){
        return date[n-1];
    }
    
    return 0;
}

void Stiva::print(){
    while(n>0){
        std::cout<<date[--n]<<" ";
    }
    std::cout<<"\n";
}