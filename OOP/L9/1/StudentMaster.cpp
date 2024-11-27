#include "StudentMaster.h"

StudentMaster::StudentMaster()
{
    cout << "constr. fara arg. StudentMaster" << endl;
    this->m_sNumeDizertatie = "";
}

StudentMaster::StudentMaster(string cnp, string nume, string adresa, int anStudiu, int notaP2, string m_sNumeDizertatie) : StudentAC(cnp, nume, adresa, anStudiu, notaP2), m_sNumeDizertatie(m_sNumeDizertatie)
{
    cout << "constr. cu arg. StudentMaster" << endl;   
}

StudentMaster::~StudentMaster(){
    cout << "destructor StudentMaster" << endl;
}

void StudentMaster::afisare(){
    this->afisareProfil();
    cout<<"Dizertatie: "<<this->m_sNumeDizertatie<<"\n\n";
}
