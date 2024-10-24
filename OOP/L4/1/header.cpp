#include <iostream>
#include "header.h"

Multimea:: Multimea(int dim){
    date = new int[dim];
    n = 0;
}   


void Multimea::add(int value){
    
    if(n == dim-1){
        std::cout<<"The array is full!\n";
        return;
    }

    for(int i=0; i<n; i++){
        if(date[i] == value){
            return;
        }
    }
    
    date[n++] = value;
}

void Multimea::remove(int value){
    for(int i=0; i<n; i++){
        if(date[i] == value){
            for(int j=i; j<n-1; j++){
                date[j] = date[j+1];
            }
            n--;
            break;
        }
    }
}

void Multimea::write(){
    for(int i=0; i<n; i++){
        std::cout<<date[i]<<" ";
    }
    std::cout<<"\n";
}
