#pragma once

class Stiva{
    private:
        int *date;
        int n;
        int dim;

    public:
        void push(int);
        int pop();
        int top();
        void print();
        Stiva();   
};
