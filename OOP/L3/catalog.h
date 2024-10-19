#pragma once

#include <iostream>
#include <stdlib.h>
#include <string.h>

typedef struct _Student{

	private:
		char *name;
		int grade;

	public:
		int getGrade(void);
		void setGrade(int value);
		char* getName(void);
		void setName(char randName[]);
		void freeMem(void);
		void read(void);
		void write(void);

}Student;

typedef int (*PFnComparison)(Student a, Student b);

typedef struct _Group{

	int nrStud;
	Student *tabStudents;

	char* groupName;
	void read(void);
	void write(void);

	PFnComparison comparator;

	void bSort(void);
	void freeMem(void);

}Group;

typedef struct _Catalog{
	int nrGroups;
	Group *tabGroups;

	void setComparator(PFnComparison comparator);
	void read(void);
	void write(void);
	void sort(void);
	void freeMemory(void);

}Catalog;

int compareNameAlphabetically(Student a, Student b);
int compareNotesDescending(Student a, Student b);
int compareNameByLengthAscending(Student a, Student b);
void swap(Student &a, Student &b);