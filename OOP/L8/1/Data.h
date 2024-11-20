#ifndef _Data_h_
#define _Data_h_
class Data
{
private:
    int an, luna, zi;

public:
    Data() {}
    Data(int an, int luna, int zi);
    int getAn();
    int getLuna();
    int getZi();
    /*returneaza 1 daca this > data2, 0 daca this <=
    data2*/
    int maiMare(Data data2);
};
#endif