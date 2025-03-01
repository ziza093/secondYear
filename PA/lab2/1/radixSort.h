#pragma once

#include <iostream>

struct Node{
    int value;
    Node *next;
};

struct List{
    Node *prim;
    Node *ultim;
};



void readList(List *&l);
void writeList(List *l);
void insertList(Node *&head, int value);
bool isEmpty(List *l);
void radixSort(List *&l, int m);