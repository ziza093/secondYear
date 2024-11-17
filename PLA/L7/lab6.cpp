#include <iostream>

extern "C" int PrintF(const char* format, ...);
extern "C" unsigned short MaxVect(unsigned short* v, size_t len);
extern "C" unsigned int MinVect(unsigned int* v, size_t len);
extern "C" long long MaxVect64(long long* v, size_t len);
extern "C" int NrAparitii(char* sir, char c);
extern "C" void SortVector(long long* v, size_t len);

int main(){
	
	unsigned short* v1 = 0;
	unsigned int* v2 = 0;
	long long* v3 = 0;
	char sir[] = "mama si tata";
	char c = 'm';

	size_t len = 0;

	std::cout << "Read the length of the array!\n";
	std::cin >> len;

	v3 = new long long[len] ;
	v1 = new unsigned short[len];
	v2 = new unsigned int[len];
	
	std::cout << "Read the array elements (beware of the data types for the vectors): ";

	for (int i = 0; i < len; i++) {
		std::cin >> v3[i];
		v1[i] = v3[i];
		v2[i] = v3[i];
	}

	std::cout << "The maximum element is: " << MaxVect(v1, len)<<std::endl;
	std::cout << "The minimum element is: " << MinVect(v2, len)<<std::endl;
	std::cout << "The maximum element on 64 is: " << MaxVect64(v3, len) << std::endl;
	std::cout << "The number of appearences of the " << c << " character is:" << NrAparitii(sir, c)<<std::endl;
	std::cout << "The sorted array is: ";
	
	SortVector(v3, len);

	for (int i = 0; i < len; i++) {
		std::cout << v3[i] << " ";
	}
	std::cout << std::endl;

	delete []v1;
	delete []v2;
	delete []v3;
	

	return 0;
}
