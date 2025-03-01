#pragma once

#include <iostream>

struct LinkedList{
    int value;
    LinkedList *next, *prev;
};

void initList(LinkedList *&head);
void printList(LinkedList *head);
void insertList(LinkedList *&head, int position, int value);
void createList(LinkedList *&head);
void insertNode(LinkedList *&head, int value);