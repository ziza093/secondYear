#pragma once
#include <iostream>

class Multimea{
private:
    int *date;
    int dim;
    int n;

public:
    Multimea(int);
    Multimea(const Multimea& m);
    void operator+=(int);
    void operator-=(int);
    Multimea &operator+=(Multimea);
    Multimea operator+(Multimea);
    Multimea operator=(Multimea);
    void write();

};

