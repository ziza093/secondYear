#include "catalog.h"


//student methods
void Student::setGrade(int value){
	grade = value;
}

int Student::getGrade(void){
	return grade;
}

void Student::setName(char randName[]){
	name = new char[strlen(randName) + 1];
	strncpy(name, randName, strlen(randName) + 1);  
}

char* Student::getName(void){
	return name;
}

void Student::read(void){
	char buffer[50];
	int n;

	std::cout<<"Read name:\n";
	std::cin>>buffer;
	setName(buffer);

	std::cout<<"Read grade:\n";
	std::cin>>n;
	setGrade(n);
}

void Student::write(void){
	std::cout<<"Name: "<<getName()<<std::endl;
	std::cout<<"Grade: "<<getGrade()<<std::endl;
}

void Student::freeMem(void){
	delete name;
	name = nullptr;
}



//group methods
void Group::freeMem(void){
	int s;

	if(groupName){
		std::cout<<"free memory for group"<<groupName<<std::endl;
		free(groupName);
		groupName = NULL;
	}

	for(s=0; s<nrStud; s++){
		tabStudents[s].Student::freeMem();
	}

	free(tabStudents);
	tabStudents = NULL;
}

void Group::read(void){
	char buffer[50];

	std::cout<<"Read the group name: ";
	std::cin>>buffer;
	
	groupName = new char[strlen(buffer) + 1];
	strncpy(groupName, buffer, strlen(buffer) + 1);

	std::cout<<"Read the number of students: ";
	std::cin>>nrStud;

	tabStudents = new Student[nrStud];
	for(int i=0; i<nrStud; i++){
		tabStudents[i].read();
	}
}

void Group::write(void){
	std::cout<<"Group name: "<<groupName<<std::endl;
	std::cout<<"Number of students: "<<nrStud<<std::endl;
	std::cout<<"The students:\n";
	
	for(int i=0; i<nrStud; i++){
		tabStudents[i].write();
	}
	
}

void Group::bSort(void){
	bool sorted = true;

	do{
		sorted = true;
		for(int i=0; i<nrStud - 1; i++){
			if(comparator(tabStudents[i], tabStudents[i+1]) == 0){
				swap(tabStudents[i], tabStudents[i+1]);
				sorted = false;
			}
		}
	}while(!sorted);
}




//catalog methods
void Catalog::setComparator(PFnComparison comparator){
	int i;
	for(i = 0; i < nrGroups; i++){
		tabGroups[i].comparator = comparator;
	}
}

void Catalog::read(void){
	std::cout<<"Read the number of groups\n";
	std::cin>>nrGroups;

	tabGroups = new Group[nrGroups];
	std::cout<<"Read the groups:\n";
	for(int i=0; i<nrGroups; i++){
		tabGroups[i].read();
	}	
}

void Catalog::write(void){
	std::cout<<"Write the number of groups: "<<nrGroups<<std::endl;
	std::cout<<"Write the groups:\n";

	for(int i=0; i<nrGroups; i++){
		tabGroups[i].write();
	}
}

void Catalog::sort(void){
	for(int i=0; i<nrGroups; i++){
		tabGroups[i].bSort();
	}
}

void Catalog::freeMemory(void){
	for(int i=0; i<nrGroups; i++){
		tabGroups[i].Group::freeMem();
	}

	free(tabGroups);
	tabGroups = nullptr;
}



//sorting methods
void swap(Student &a, Student &b){
	Student aux = a;
	a = b;
	b = aux;
}

int compareNameByLengthAscending(Student a, Student b){
	int rez = strlen(a.getName()) - strlen(b.getName());
	if(rez > 0){
		rez = 1;
	}
	else{
		if(rez < 0){
			rez = -1;
		}
	}
	
	return rez;
}

int compareNameAlphabetically(Student a, Student b){
	if (strcmp(a.getName(), b.getName()) <= 0)
        return 1;

    return 0;
}

int compareNotesDescending(Student a, Student b){
	if (a.getGrade() >= b.getGrade())
        return 1;

    return 0;
}