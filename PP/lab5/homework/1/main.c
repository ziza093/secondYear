#include <stdio.h>
#include <regex.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <errno.h>
#include <fcntl.h>
#include <unistd.h>

// structure for message queue
struct mesg_buffer {
    long mesg_type;
    char mesg_text[1000];
} message;


int validate_html(const char* html_content){

    FILE *output=fopen("output.html", "w");
    regex_t regex;
    const char* pattern = "<!DOCTYPE html>\\s*<html[^>]*>\\s*<title>[^<]*</title>\\s*(<p>[^<]*</p>\\s*)+</html>";
    int ret = regcomp(&regex, pattern, REG_EXTENDED | REG_NOSUB);

    if (ret) {
        fprintf(stderr, "Could not compile regex\n");
        return 0;
    }

    
    ret = regexec(&regex, html_content, 0, NULL, 0);
    if (!ret) {
        printf("The file contains valid HTML structure\n");

        if(!output){
            fprintf(stderr, "The file didnt open!\n");
            return -1;
        }else{
            fprintf(output, "%s", html_content);
            fclose(output);
        }

    } else if (ret == REG_NOMATCH) {
        printf("The file does not contain valid HTML structure\n");
    } else {
        char error_buffer[100];
        regerror(ret, &regex, error_buffer, sizeof(error_buffer));
        fprintf(stderr, "Regex match failed: %s\n", error_buffer);
    }
 
    return ret == 0;
}


int main(void){

    
    key_t key;
    int msgid;
    message.mesg_type = 1;


    // ftok to generate unique key
    key = ftok("message_queue_name", 'B');
    printf("key is %d \n", key);

    // msgget creates a message queue and returns identifier
    msgid = msgget(key, 0666 | IPC_CREAT);
    
    //msgrcv to receive message
    if(msgrcv(msgid, &message, sizeof(message), 1, 0) == -1){
        perror("Error while trying to receive the message!\n");
    }else{
        printf("Message received!\n");
    }

    // printf("Date Received is: %s \n", message.mesg_text);

    if(validate_html(message.mesg_text)){
        printf("The file \"output.html\" has been created!\n");       
    }
    
    msgctl(msgid, IPC_RMID, NULL);

    return 0;
}
