#include "Data.h"
#include "Persoana.h"
#include <stdlib.h>
#include <string.h>

int main()
{

    Student *student[4];

    char *name = new char[10];
    char *surname = new char[10];

    for (int i = 0; i < 4; i++)
    {
            int zi = 1 + rand() % 32;
            int luna = 1 + rand() % 12;
            int an = 2004 + rand() % 20;
            Data *data = new Data(zi, luna, an);

            //name[i] = 'a' + rand() % 26;
            name[0] = 'A' + rand() % 26; // 'A' to 'Z'
            // Fill the rest of the name with lowercase letters
            for (int i = 1; i < 10; ++i){
                name[i] = 'a' + rand() % 26; // 'a' to 'z'
            }
            // Null-terminate the string
            name[10] = '\0';

            surname[0] = 'A' + rand() % 26; // 'A' to 'Z'
            // Fill the rest of the name with lowercase letters
            for (int i = 1; i < 10; ++i){
                surname[i] = 'a' + rand() % 26; // 'a' to 'z'
            }
            // Null-terminate the string
            surname[10] = '\0';


            //surname[i] = 'a' + rand() % 26;
            int gr = 1001 + rand() % 999;

            student[i] = new Student(name, surname, data, gr);
        
    }

    for(int i=0; i<3; i++){
        for(int j=i+1; j<4; j++){
            if(student[i]->getGrupa() > student[j]->getGrupa()){
                Student *aux;
                aux = student[i];
                student[i] = student[j];
                student[j] = aux;
            }

        }
    }


    for (int i = 0; i < 4; i++)
    { // 3 because the problem says there are 3 people
        student[i]->afisare();
    }

    return 0;
}