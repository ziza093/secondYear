#include "Persoana.h"
#include "Student.h"
#include "StudentMaster.h"

void afisareProfil(StudentAC s){
    s.afisareProfil();
}


int main()
{
    /*PersoanaAC p1("1234567890123", "Ana", "Iasi");
    p1.afisareProfil();
    StudentAC s2;
    s2.afisareProfil();
    StudentAC s1("1234567890122", "Ion", "Vaslui", 2, 10);
    s1.schimbareAdresa("Bucuresti");
    s1.inscriereAnStudiu(3);
    afisareProfil(s1);
    StudentAC *s3 = s2.compareNotes(&s1);
    s3->afisareProfil();
*/
    StudentMaster m1;
    StudentMaster m2("1234567890123", "Mihai", "Myl-bey", 123, 10, "Mda");
    StudentAC *notamax = new StudentAC;
    int index = 0;
    StudentMaster *vect[5];

    for(int i=0; i<5; i++){
        int nota = rand() % 11;
        vect[i] = new StudentMaster("2314567890", "Mihai", "Myl-bey", 123, nota, "Mna");
    }

    for(int i=0; i<5; i++){
        vect[i]->afisare();
    }

    for(int i=0; i<4; i++){
        StudentAC *temp = vect[i]->compareNotes(notamax);

        if(notamax != temp){
            notamax = temp;
            index = i;
        }
    }

    vect[index]->afisare();

    return 0;
}