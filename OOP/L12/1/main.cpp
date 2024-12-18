#include <iostream>

template <typename T>

void sortare(T* vect, int n){

    for (int i = 0; i<n; i++){
        for (int j = 0; j<n-1; j++){
            if (vect[j] > vect[j+1]){
                T aux = vect[j];
                vect[j] = vect[j+1];
                vect[j+1] = aux;
            }
        }
    }
}


int main(){

    int vect[] = {5, 2, 0, -1, 1, 7};
    float vect2[] = {5.1, 2.2, 0.5, -1.2, 1.1, 7.4};

    sortare(vect, 6);
    sortare(vect2, 6);

    for(int i = 0; i < 6; i++){
        std::cout<<vect[i]<<" ";
    }
    std::cout<<std::endl<<std::endl;

    for(int i = 0; i < 6; i++){
        std::cout<<vect2[i]<<" ";
    }
    std::cout<<std::endl<<std::endl;

    return 0;
}