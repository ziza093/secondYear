#include <string.h>
#include <stdlib.h>
#include <stdio.h>

int str_length(char *s){
    
    int length = 0;

    while(s[length] != '\0'){
        length++;
    }

    return length;
}

char * strdel(char *s, int pos, int n){
    char *t = 0;
    t = (char*)malloc(sizeof(char));

    for(int i = 0; i < str_length(s); ++i){
        if(i+1 == pos){
            strcpy(t, s+pos+n + 1);
            s[i+1] = '\0';
            break;
        }
    }
    
    strcat(s, t);

    memset(t, 0, sizeof(char));
    free(t);
    t = 0;

    return s;
}



char *strins(char *s1, int pos, char *s2){
    
    char *aux = 0;
    aux = (char*)malloc(sizeof(char));

    for(int i=0; i<strlen(s1); i++){
        if(i+1 == pos){
            strcpy(aux, s1+i+1);
            s1[i+1] = '\0';

            strcat(s1, s2);
            strcat(s1,aux);
            s1[strlen(s1)] = '\0';

            break;
        }
    }

    return s1;
}

int eq_mask(char *sir, char *mask){
    
    for(int i=0; i<strlen(sir); i++){
        if(sir[i] != mask[i] && mask[i] != '?'){
            return 0;
        }
    }


    return 1;
}

int eqcuv(char *word, char **table){
    int ok = 0;

    for(int i=0; table[i]; i++){
        if(strcmp(word, table[i]) == 0){
            ok = 1;
        }
    }

    return ok;
}
