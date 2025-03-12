#include "mergeSort.h"

void citireVector(int a[], int size)
{
    for(int i=0; i<size; i++){
        std::cin>>a[i];
    }
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

void afisareVector(int a[], int size)
{
    for(int i=0; i<size; i++){
        std::cout<<a[i]<<" ";
    }

    std::cout<<"\n";
}
