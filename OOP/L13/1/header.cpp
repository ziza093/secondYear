#include "header.h"

int upperChars(std::string s)
{
    int cnt=0, i=0;

    while(s[i]){
        
        if(isupper(s[i])){
            cnt++;
        }

        i++;
    }

    return cnt;
}

int stringLength(std::string s)
{
    std::string::iterator si;

    int length=0;

    for(si = s.begin(); si != s.end(); si++){
            length++;
    }

    return length;
}

StudentAC::StudentAC()
{
    this->nota = 0;
    this->nume = nullptr;
}

StudentAC::StudentAC(std::string nume, int nota)
{
    this->nume = nume;
    this->nota = nota;
}

void StudentAC::afisare()
{
    std::cout<<"Numele este: "<<this->nume<<std::endl;
    std::cout<<"Nota este: "<<this->nota<<std::endl;
}

void StudentAC::modificareNota(int nouaNota)
{
    this->nota = nouaNota;
}

bool StudentAC::operator<(const StudentAC &s)
{
    return nota<s.nota;
}

void printVector(std::vector <StudentAC> v){
    std::vector <StudentAC>::iterator it;

    for(it = v.begin(); it!= v.end(); it++){
        (*it).afisare();
    }
}

std::vector <StudentAC> transform(StudentAC *v, int n){
    std::vector <StudentAC> ans;

    for(int i = 0; i<n; i++){
        ans.push_back(v[i]);

    }
    return ans;
}

bool compare(StudentAC a, StudentAC b)
{
    return a<b;
}
