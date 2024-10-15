#include <iostream>
#include <string.h>

struct student{
    int numar_matricol;
    char nume[20];
    char gen;
    float nota;
    void (*read)(student*st);
    void (*write)(student*st);
    void Read(void);
    void Write(void);
};

struct catalog{
    int count;
    char **name;
    void AlphaSort(void);
    void LengthSort(void);
    void Read(void);
    void Write(void);

};

void catalog::AlphaSort(void){
    for(int i = 0; i < count; i++){
        for(int j = 0; j < count; j++)
            if(strcmp(name[i], name[j]) < 0){
                char aux[100];
                strcpy(aux, name[i]);
                strcpy(name[i], name[j]);
                strcpy(name[j], aux);
            }

    }
}

void catalog::LengthSort(void){
    for(int i = 0; i < count; i++){
        for(int j = 0; j < count; j++)
            if(strlen(name[i]) < strlen(name[j])){
                char aux[100];
                strcpy(aux, name[i]);
                strcpy(name[i], name[j]);
                strcpy(name[j], aux);
            }

    }
}

void catalog::Read(void){



    std::cout<<"Nr de studenti: ";
    std::cin>>count;

    name = new char*[count];

    for(int i = 0; i < count; i++){
        name[i] = new char[20];
        std::cin>>name[i];
    }
}

void catalog::Write(void){
    for(int i = 0; i < count; i++){
        std::cout<<name[i]<<"\n";
    }
    
    std::cout<<"\n";
}

void student::Read(void){
    std::cout<<"Numar matricol: ";
    std::cin>>numar_matricol;

    std::cout<<"Nume: ";
    std::cin>>nume;

    std::cout<<"Gen: ";
    std::cin>>gen;

    std::cout<<"Nota: ";
    std::cin>>nota;
} 


void student::Write(void){
    std::cout<<"\n";
    std::cout<<"Numarul matricol: "<<numar_matricol<<std::endl;
    std::cout<<"Nume: "<<nume<<std::endl;
    std::cout<<"Gen: "<<gen<<std::endl;
    std::cout<<"Nota: "<<nota<<std::endl;
    
    std::cout<<"\n";
}


void ReadData(student* st){
    std::cout<<"Numar matricol: ";
    std::cin>>st->numar_matricol;

    std::cout<<"Nume: ";
    std::cin>>st->nume;

    std::cout<<"Gen: ";
    std::cin>>st->gen;

    std::cout<<"Nota: ";
    std::cin>>st->nota;
}

void WriteData(student* st){
    std::cout<<"\n";
    std::cout<<"Numarul matricol: "<<st -> numar_matricol<<std::endl;
    std::cout<<"Nume: "<<st -> nume<<std::endl;
    std::cout<<"Gen: "<<st -> gen<<std::endl;
    std::cout<<"Nota: "<<st -> nota<<std::endl;
}

void firstPart(void){
    
    int students = 0;
    student *v = 0;

    std::cout<<"Read the number of students:\n";
    std::cin>>students;
    
    v = new student[students];
    
    for(int i = 0; i < students; i++){
        v[i].read = ReadData;
        v[i].write = WriteData;
    }

    for(int i = 0; i < students; i++){
        v[i].read(&v[i]);     
    }

    for(int i = 0; i < students; i++){
        v[i].write(&v[i]);     
    }
    /*
    These 2 loops are the same as the ones above. They have the same 
    functionality, but with a different approach


    for(int i = 0; i < students; i++){
        v[i].Read();
    }

    for(int i = 0; i < students; i++){
        v[i].Write();
    }
    */

    delete[] v;

}

void secondPart(void){
    catalog cat;
    
    cat.Read();

    std::cout<<"\nDisplay by length:\n";

    cat.LengthSort();
    cat.Write();

    std::cout<<"Display alphabetically:\n";
    
    cat.AlphaSort();
    cat.Write();
}


int main(void){

    int choice = 0;
    
    do{
        std::cout<<"Choose an option from the menu:\n";
        std::cout<<"0. Exit\n";
        std::cout<<"1. First part\n";
        std::cout<<"2. Second part\n";

        std::cin>>choice;

        switch(choice){
    
            case 0: 
                return 0;
                break;

            case 1: 
                firstPart();
                break;
        
            case 2:
                secondPart();
                break;

            default:
                std::cout<<"The option you chose doesnt exist in the menu. Please try again!\n";
                break;
        }
    }while(choice < 0 || choice > 2);

    return 0;
}
