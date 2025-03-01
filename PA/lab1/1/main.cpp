#include "list.h"

int main(void){

    LinkedList *list = new LinkedList;

    initList(list);         
    createList(list);       //to have a list to insert into
    printList(list);

    insertList(list, 3, 69);
    printList(list);
    
    insertList(list, 0, 21);
    printList(list);

    return 0;
}