#include "catalog.h"

int main(void){
	
	int choice;
	PFnComparison anComparisonFunctionPointer;
	Catalog year1, year2, year3, year4;
	Catalog master[2];

	year2.read();
	year2.write();

	do{
		do{
			std::cout<<"Choose an option from the menu\n";
			std::cout<<"0. Exit\n";
			std::cout<<"1. Sort names alphabetically\n";
			std::cout<<"2. Sort descending by grade\n";
			std::cout<<"3. Sort by name length\n";
			std::cin>>choice;

		}while((choice < 0) || (choice > 3));

		switch(choice){
			
			case 1:
				anComparisonFunctionPointer = compareNameAlphabetically;
				year2.setComparator(anComparisonFunctionPointer);
				break;

			case 2:
				break;

			case 3:
				break;
		}

	}while(choice);
	
	year2.freeMemory();

	return 0;
}
