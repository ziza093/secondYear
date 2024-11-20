#include <iostream>
#include <string.h>
#include "Data.h"
#include "Persoana.h"
using namespace std;
Persoana::Persoana(char *nume, char *prenume, Data *dataNastere)
{
    this->nume = new char[strlen(nume) + 1];
    this->prenume = new char[strlen(prenume) + 1];
    strcpy(this->nume, nume);
    strcpy(this->prenume, prenume);
    this->dataNastere =
        new Data(*dataNastere);
}
Persoana::~Persoana()
{
    delete[] nume;
    delete[] prenume;
    delete dataNastere;
}
char *Persoana::getNume()
{
    return nume;
}
char *Persoana::getPrenume()
{
    return prenume;
}
Data *Persoana::getDataNastere()
{
    return dataNastere;
}
void Persoana::afisarePersoana()
{
    cout << nume << " " << prenume
         << ", data nastere: "
         << dataNastere->getAn() << "."
         << dataNastere->getLuna() << "."
         << dataNastere->getZi();
}
void Persoana::afisare()
{
    afisarePersoana();
    cout << endl;
}


void Student::afisare()
{
    afisarePersoana();
    cout<<" ,Grupa: "<<this->grupa<<"\n";
}

int Student::getGrupa(){
    return this->grupa;
}
