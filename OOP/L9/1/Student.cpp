#include "Student.h"

StudentAC::StudentAC()
{
    cout << "constr. fara arg. StudentAC" << endl;
    m_ianStudiu = 0;
    m_inotaP2 = 0;
}
StudentAC::StudentAC(string cnp, string nume, string adresa,
                     int anStudiu, int notaP2) : PersoanaAC(cnp, nume, adresa), m_ianStudiu(anStudiu),
                                                 m_inotaP2(notaP2)
{
    cout << "constr. cu arg. StudentAC" << endl;
}
StudentAC::~StudentAC()
{
    cout << "destructor StudentAC" << endl;
}

void StudentAC::afisareProfil(){
    PersoanaAC::afisareProfil();
    cout<<"Afisare profil student: ";
    cout<<"An studiu: "<<m_ianStudiu<<"\nNota: "<<m_inotaP2<<"\n";
}

void StudentAC::inscriereAnStudiu(int anStudiuNou){
    this->m_ianStudiu = anStudiuNou;
}

StudentAC *StudentAC::compareNotes(StudentAC *s)
{
    if(this->m_inotaP2 >= s->m_inotaP2)
        return this;
    
    return s;
}
