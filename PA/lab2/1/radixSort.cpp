#include "radixSort.h"

void readList(List *&l)
{
    std::cout<<"";
}

void writeList(List *l)
{
}

void insertList(Node *&head, int value)
{
    Node *temp = new Node;
    temp->value = value;

    head->next = temp;
    temp->next = nullptr;
    head = temp;
}

bool isEmpty(List *l)
{
    return (l->prim == l->ultim) && (l->ultim == nullptr); 
}

void radixSort(List *&l, int m)
{

    List *pachet[10];

    for(int i=m-1; i>=0; i--){
        for(int j=0; j<9; j++){
            pachet[j] = new List;
        }
        while(isEmpty(l)){
            //w = //citeste(l,0)"?"
            //elimina(l,0)
            //insereaza(pachet[w[i]], w)
        }

        for(int j=0; j<9; j++){
            //concateneaza(l, pachet[j])
        }
    }
}
