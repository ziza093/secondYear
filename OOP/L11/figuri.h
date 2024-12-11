#ifndef _figuri_
#define _figuri_
#pragma warning(disable : 4996)


class Punct{
    
    public:
        int x, y;

    public:
        Punct(int x = 0, int y = 0);
        void afisare();
};


class Figura
{
public:
    virtual ~Figura() {}
    virtual float arie() = 0;
    virtual void afisare() = 0;
    virtual float perimetru() = 0;
    
};
class Dreptunghi : public Figura
{
private:
    Punct p1, p2;

public:
    Dreptunghi(Punct p1, Punct p2);
    float arie();
    float perimetru();
    void afisare();
};
class Cerc : public Figura
{
private:
    Punct p;
    int r;

public:
    Cerc(Punct p1, int r);
    float arie();
    float perimetru();
    void afisare();
};

class Triunghi : public Figura{
    Punct p1,p2,p3;

    public: Triunghi(Punct p1, Punct p2, Punct p3);
            float arie();
            float perimetru();
            void afisare();

};


Figura *afisarePerimMax(Figura **f, int n);
void sortareFiguri(Figura **f, int n);
void afisareVectorFiguri(Figura **f, int n);

#endif