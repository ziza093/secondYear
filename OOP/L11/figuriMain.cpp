#include <conio.h>
#include <iostream>
#include "Figuri.h"
int main()
{
    // Figura *dr = new Dreptunghi(1, 2, 4, 4);
    // Figura *cerc = new Cerc(1, 1, 3);
    // Figura *tr = new Triunghi(0,0,2,0,1,2);
    Figura *figuri[3];
    Figura *perimetru;
    Punct p1(1,3);
    Punct p2(1,4);
    Punct p3(2,2);

    figuri[0] = new Dreptunghi(p1,p2);
    figuri[1] = new Cerc(p1,3);
    figuri[2] = new Triunghi(p1,p2,p3);
        
    figuri[0]->afisare();
    figuri[1]->afisare();
    figuri[2]->afisare();

    perimetru = afisarePerimMax(figuri, 3);
    std::cout<<"Figura cu perimetrul maxim este: ";
    perimetru->afisare();
    std::cout<<std::endl;

    afisareVectorFiguri(figuri, 3);
    std::cout<<std::endl;
    sortareFiguri(figuri, 3);
    afisareVectorFiguri(figuri, 3);
    std::cout<<std::endl;

    delete figuri[0];
    delete figuri[1];
    delete figuri[2];

    return 0;
}