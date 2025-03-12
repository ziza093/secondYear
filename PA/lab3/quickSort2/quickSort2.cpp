#include "quickSort2.h"

void citire(int a[], int size)
{
    std::cout<<"Citeste elementele vect: ";

    for(int i=0; i<size; i++){
        std::cin>>a[i];
    }
}

void afisare(int a[], int size)
{
    for(int i=0; i<size; i++){
        std::cout<<a[i]<<" ";
    }
    std::cout<<"\n";
}

void quickSort2(int a[], int p, int q)
{

    while(p < q){
        
        int k = 0;
        k = partitioneaza(a,p,q,k);

        if(k-p > q-k){
            quickSort2(a,k+1,q);
            q = k-1;
        }else{
            quickSort2(a,p,k-1);
            p=k+1;
        }
    }
}

int partitioneaza(int a[], int p, int q, int k)
{
    int x = a[p];
    int i = p+1;
    int j = q;

    while(i <= j){
        if(a[i] <= x){
            i++;
        }
        
        if(a[j] >= x){
            j--;
        }
        
        if(i<j){
            if(a[i] > x && x > a[j]){
                std::swap(a[i], a[j]);
                i++;
                j--;
            } 
        }
        k = i-1;
        std::swap(a[p], a[k]);

    } 
    return k;
}