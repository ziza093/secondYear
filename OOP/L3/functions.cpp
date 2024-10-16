#include "catalog.h"

void Catalog::setComparator(PFnComparison comparator){
	int i;
	for(i = 0; i < nrGroups; i++){
		tabGroups[i].comparator = comparator;
	}
}

void Group::freeMem(void){
	int s;

	if(groupName){
		std::cout<<"free memory for group"<<groupName<<std::endl;
		free(groupName);
		groupName = NULL;
	}

	for(s=0; s<nrStud; s++){
		tabStudents[s].freeMem();
	}

	free(tabStudents);
	tabStudents = NULL;
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

void setGrade(int value){
	grade = value;
}

int getGrade(){
	return grade;
}

void setName(char randName[]){
	name = new char[strlen(randName)];
	strncpy(name, randName, strlen(randName));  
}

char* getName(){
	return name;
}
