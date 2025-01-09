#include "header.h"

int main(void){

    std::string test = "MAMaSiTaTa";

    std::cout<<"The number of upper chars is: "<<upperChars(test)<<std::endl;
    std::cout<<"The length of the string is: "<<stringLength(test)<<std::endl;

    StudentAC s1("Ion", 10);
    StudentAC s2("Ana", 10);
    StudentAC s3("Ioana", 9);
    
    s1.afisare();
    s1.modificareNota(9);
    s1.afisare();

    StudentAC *vs = new StudentAC[3] {StudentAC("Gion", 7), StudentAC("Maria", 8), StudentAC("Mihai", 9)};
    std::vector<StudentAC> s = transform(vs,3);

    printVector(s);
    sort(s.begin(), s.end(), compare);

    printVector(s);
   





    return 0;
}