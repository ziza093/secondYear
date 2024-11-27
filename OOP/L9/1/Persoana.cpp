#include "Persoana.h"

PersoanaAC::PersoanaAC()
{
    cout << "constr. fara arg. PersoanaAC" << endl;
    m_sCnp = string(13, '0');
    m_sNume = "";
    m_sAdresa = "";
}
PersoanaAC::PersoanaAC(string cnp, string nume, string adresa)
{
    cout << "constr. cu arg. PersoanaAC" << endl;
    m_sCnp = cnp;
    m_sNume = nume;
    m_sAdresa = adresa;
}
PersoanaAC::~PersoanaAC()
{
    cout << "destructor PersoanaAC" << endl;
}

void PersoanaAC::afisareProfil(){
    cout<<"Adresa: "<<m_sAdresa<<"\nCnp: "<<m_sCnp<<"\nNume: "<<m_sNume<<"\n";
}

void PersoanaAC::schimbareAdresa(string adresaNoua){
    this->m_sAdresa = adresaNoua;
}
