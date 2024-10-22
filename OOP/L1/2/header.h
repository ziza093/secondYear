#ifndef HEADER_H_
#define HEADER_H_


int str_length(char *s);
char *strdel(char *s, int pos, int n);
char *strins(char *s1, int pos, char *s2);
int eq_mask(char *sir, char *mask);
int eqcuv(char *word, char **table);

#endif
