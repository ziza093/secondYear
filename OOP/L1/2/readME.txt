This problem has a few subsections.
I had to write a few functions with various uses.

A function to determine the size of an string.

A function to delete 'n' number of characters from a specified position and returns
the adress of the modified string.

A function to add an 's2' string in an specified position inside an 's1' string
(It is assumed that there is enough space to fit the 's2' string) and returns
the adress of the 's1' string.

A function to check if 2 strings are identical, but with a twist (the 2nd string 
can have an special character '?' which replaces any character).
ex: 'abcde' is identical with '?bc?e'

A function to determine whether a word can be found in an array of strings.
Use this format for the main function:

int main(void) {
	char *tablou[100] = {"curs1", "curs2", "curs3"};
	char *cuv1 = "curs2", *cuv2 = "curs5";
	printf("curs2 %s in tablou\n",(eqcuv(cuv1, tablou))?"este":"nu este");
	printf("curs5 %s in tablou\n",(eqcuv(cuv2, tablou))?"este":"nu este");
}
