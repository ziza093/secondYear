#pragma once
#include <iostream>
#include <string.h>
#include "Persoana.h"

using namespace std;

class StudentAC : public PersoanaAC{
    int m_ianStudiu;
    int m_inotaP2;

public:
    StudentAC();
    StudentAC(string cnp, string nume, string adresa, int anStudiu, int notaP2);
    ~StudentAC();
    void afisareProfil();
    void inscriereAnStudiu(int anStudiuNou);
    StudentAC* compareNotes(StudentAC*);
};