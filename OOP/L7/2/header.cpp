#include <iostream>
#include "header.h"

Multimea:: Multimea(int dim){
    date = new int[dim];
    this->dim = dim;
    n = 0;
}

Multimea::Multimea(const Multimea& m){
    this->dim = m.dim;
    this->n = m.n;
    this->date = new int[this->dim];

    for(int i=0; i<this->n; i++){
        this->date[i] = m.date[i];
    }
}

void Multimea::operator+=(int value){
    
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

void Multimea::operator-=(int value){
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

Multimea &Multimea::operator+=(Multimea m){

    for(int i=0; i<this->dim && this->n<this->dim; i++){
        if(i < m.n){
            this->date[this->n++] = m.date[i];
        }
    }


    if(this->n == this->dim){
        std::cout<<"You reached the maximum dimension!\n";
    }

    return *this;
}

Multimea Multimea::operator+(Multimea m){

    int size = this->n + m.n;

    Multimea temp(size);

    temp.n = size;

    for(int i=0; i<temp.dim; i++){
        if(i < this->n){
            temp.date[i] = this->date[i];
        }
        else{
            if(i-this->n < m.n){
                temp.date[i] = m.date[i-this->n];
            }
        }
    }

    return temp;
}

Multimea Multimea::operator=(Multimea m){
     
    this->n = this->n > m.n ? this->n : m.n;

    for(int i=0; i<this->n; i++){
        if(i < m.n){
            this->date[i] = m.date[i];
        }
    }
    
    return *this;
}

void Multimea::write(){
    for(int i=0; i<n; i++){
        std::cout<<date[i]<<" ";
    }
    std::cout<<"\n";
}
