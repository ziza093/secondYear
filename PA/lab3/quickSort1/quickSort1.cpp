#include "quickSort1.h"

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

void quickSort1(int a[], int p, int q)
{
    if(p < q){
        int k = 0;
        k = partitioneaza(a,p,q,k);
        quickSort1(a, p, k-1);
        quickSort1(a, k+1, q);
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