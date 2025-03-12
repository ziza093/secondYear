#include "header.h"

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
    std::cout<<"\n\n";
}

void mergeSort(int a[], int p, int q)
{
    if(p < q){
        int m = (p+q)/2;
        
        mergeSort(a, p, m);
        mergeSort(a, m+1, q);
        
        int temp[100] = {0};
        interclasare(a, p, q, m, temp);

        for(int i=p; i<=q; i++){
            a[i] = temp[i-p];  
        }
    }
}


void interclasare(int a[], int p, int q, int m, int temp[])
{
    int i = p;
    int j = m+1;
    int k = -1;
    
    while(i <= m && j <= q){
        k++;
        if(a[i] <= a[j]){
            temp[k] = a[i];
            i++;
        }else{
            temp[k] = a[j];
            j++;
        }
    }

    while(i <= m){
        k++;
        temp[k] = a[i];
        i++;
    }

    while(j <= q){
        k++;
        temp[k] = a[j];
        j++;
    }
}

void quickSort1(int a[], int p, int q)
{
    if(p < q){
        int k = 0;
        partitioneaza(a,p,q,k);
        quickSort1(a, p, k-1);
        quickSort1(a, k+1, q);
    }
}


void partitioneaza(int a[], int p, int q, int &k)
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
        
    }
     
    k = i-1;
    std::swap(a[p], a[k]);
}


void quickSort2(int a[], int p, int q)
{

    while(p < q){
        
        int k = 0;
        partitioneaza(a,p,q,k);

        if(k-p > q-k){
            quickSort2(a,k+1,q);
            q = k-1;
        }else{
            quickSort2(a,p,k-1);
            p=k+1;
        }
    }
}

void copiere(int a[], int b[], int size)
{
    for(int i=0; i<size; i++){
        b[i] = a[i];
    }
}
