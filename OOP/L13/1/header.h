#pragma once

#include <iostream>
#include <string.h>
#include <vector>
#include <algorithm>

class StudentAC
{
    std::string nume ;
    int nota;

public:
    StudentAC();
    StudentAC(std::string nume, int nota);
    void afisare();
    void modificareNota(int nouaNota);
    bool operator<(const StudentAC &s);
};

int upperChars(std::string s);
int stringLength(std::string s);
void printVector(std::vector <StudentAC> v);
std::vector <StudentAC> transform(StudentAC *v, int n);
bool compare(StudentAC a, StudentAC b);
