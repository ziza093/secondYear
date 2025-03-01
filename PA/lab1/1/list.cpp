#include "list.h"

void initList(LinkedList *&head)
{
    head->next = nullptr;
    head->prev = nullptr;
}

void printList(LinkedList *head)
{
    while(head->next != nullptr){
        std::cout<<head->value<<" ";
        head = head->next;
    }

    std::cout<<"\n";
}

void insertList(LinkedList *&head, int position, int value)
{
    LinkedList *temp = head;
    int counter = 0;

    if(position == 0){

        LinkedList *inserted = new LinkedList;

        inserted->value = value;
        inserted->prev = 0;
        inserted->next = head;
        head->prev = inserted;

        head = inserted;

        return;
    }

    while(temp->next != nullptr){
        
        if(counter == position-1){
            LinkedList *aux = temp->next; //keep the rest of the list
            LinkedList *inserted = new LinkedList;

            inserted->value = value;
            temp->next = inserted;
            inserted->prev = temp;
            inserted->next = aux;   

            break;
        }

        temp = temp->next;
        counter++;
    }

    //head = temp;
}

void createList(LinkedList *&head)
{
    int value;

    std::cout<<"Read untill 0!\n";
    std::cin>>value;

    while(value){
        insertNode(head, value);
        std::cin>>value;
    }
}

void insertNode(LinkedList *&head, int value)
{
    //insert after head
    LinkedList *temp = new LinkedList;

    temp->value = value;
    head->prev = temp;
    temp->next = head;
    temp->prev = nullptr;
    head = temp;
}


