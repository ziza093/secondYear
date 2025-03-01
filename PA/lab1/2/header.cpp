#include "header.h"

void readMatrix(char a[n][n])
{
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            std::cin>>a[i][j];
        }
    }
}

void writeMatrix(char a[n][n])
{
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            std::cout<<a[i][j]<<" ";
        }
        std::cout<<"\n";
    }

    std::cout<<"\n";
}

void rotateMatrix(char a[n][n])
{
    char b[n][n] = {};

    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            b[i][j] = a[j][n-i-1];
        }
    }

    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            a[i][j] = b[i][j];
        }
    }

}
