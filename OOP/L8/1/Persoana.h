#include "Data.h"
#ifndef _Persoana_h_
#define _Persoana_h_
#pragma warning(disable : 4996)

class Persoana{
private:
    char *nume, *prenume;
    Data *dataNastere;

protected:
    void afisarePersoana();

public:
    Persoana() {nume = nullptr, prenume = nullptr, dataNastere = nullptr ;};
    Persoana(char *nume, char *prenume, Data *dataNastere);
    ~Persoana();
    char *getNume();
    char *getPrenume();
    Data *getDataNastere();
    void afisare();
};

class Student: public Persoana{
    int grupa;

    public:
        Student() : Persoana(){this->grupa = 0 ;};
        Student(char *nume, char *prenume, Data *dataNastere, int grupa): Persoana(nume, prenume, dataNastere){this->grupa = grupa;};
        void afisare();
        int getGrupa();
};

#endif