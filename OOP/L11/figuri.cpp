#include <iostream>
#include <math.h>
#include "Figuri.h"
using namespace std;
Dreptunghi::Dreptunghi(Punct p1, Punct p2)
{
    this->p1 = p1;
    this->p2 = p2;
}
float Dreptunghi::arie()
{
    return (float)(p2.x - p1.x) * (p2.y - p1.y);
}
float Dreptunghi::perimetru()
{
    return 2*(this->p2.x - this->p1.x) + 2*(this->p2.y - this->p1.y);
}
void Dreptunghi::afisare()
{
    cout << "Dreptunghi cu coordonatele ";
    p1.afisare();
    p2.afisare();
    cout << " si aria " << arie() << endl;
}
Cerc::Cerc(Punct p1, int r)
{
    this->p = p1;
    this->r = r;
}
float Cerc::arie()
{
    const float PI = 3.14F;
    return PI * r * r;
}
float Cerc::perimetru()
{
    const float PI = 3.14F;
    return 2*PI*r;
}
void Cerc::afisare()
{
    cout << "Cerc cu coordonatele (";
    p.afisare();
    cout<<"), raza " << r << " si aria " << arie() << endl;
}

Triunghi::Triunghi(Punct p1, Punct p2, Punct p3)
{
    this->p1 = p1;
    this->p2 = p2;
    this->p3 = p3;
}

float Triunghi::arie()
{
    float a = sqrt((this->p1.x - this->p2.x) * (this->p1.x - this->p2.x) + (this->p1.y - this->p2.y) * (this->p1.y - this->p2.y)); 
    float b = sqrt((this->p1.x - this->p3.x) * (this->p1.x - this->p3.x) + (this->p1.y - this->p3.y) * (this->p1.y - this->p3.y)); 
    float c = sqrt((this->p2.x - this->p3.x) * (this->p2.x - this->p3.x) + (this->p2.y - this->p3.y) * (this->p2.y - this->p3.y)); 
    float p = (a+b+c)/2;
    
    return sqrt(p*(p-a)*(p-b)*(p-c));
}

float Triunghi::perimetru()
{
    float a = sqrt((this->p1.x - this->p2.x) * (this->p1.x - this->p2.x) + (this->p1.y - this->p2.y) * (this->p1.y - this->p2.y)); 
    float b = sqrt((this->p1.x - this->p3.x) * (this->p1.x - this->p3.x) + (this->p1.y - this->p3.y) * (this->p1.y - this->p3.y)); 
    float c = sqrt((this->p2.x - this->p3.x) * (this->p2.x - this->p3.x) + (this->p2.y - this->p3.y) * (this->p2.y - this->p3.y)); 
    
    return a+b+c;
}

void Triunghi::afisare()
{
    cout << "Triunghi cu coordonatele (";
    p1.afisare();
    p2.afisare();
    p3.afisare();
    cout<<") si aria " << arie() << endl;
}

Figura *afisarePerimMax(Figura **f, int n)
{
    float max = f[0]->perimetru();
    Figura *ans = nullptr;

    for(int i=0; i<n; i++){
        if(f[i]->perimetru() > max){
            max = f[i]->perimetru();
            ans = f[i];
        }
    }

    return ans;
}


void sortareFiguri(Figura **f, int n)
{
    Figura *ans = nullptr;

    for(int i=0; i<n-1; i++){
        for(int j=i+1; j<n; j++){
            if(f[i]->arie() > f[j]->arie()){
                ans = f[i];
                f[i] = f[j];
                f[j] = ans;
            }
        }
    }
}

void afisareVectorFiguri(Figura **f, int n)
{
    for(int i=0; i<n; i++){
        f[i]->afisare();
    }
}

Punct::Punct(int x, int y)
{
    this->x = x;
    this->y = y;
}

void Punct::afisare()
{
    cout << "Punct cu coordonatele ("<< x << "," << y <<")"<<endl;
}
