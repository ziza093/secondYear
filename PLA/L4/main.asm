include win64.inc
.data
	string db "mother", 0 ; declarare sir de caractere
.code
 main proc								; main() {
	;xor ax, ax							; AX = 0 (initializare contor)
	;lea rdi, string						; RDI = pointer la sirul de caractere "string"
;@@:										; eticheta anonima pentru inceputul buclei WHILE
	;cmp byte ptr [rdi], 0				; compar caracterul curent din sir cu \0
	;jz @F								; daca ==0 parasesc bucla WHILE (salt la @@ jos)
	;inc ax								; daca nu: AX++ (incrementez contor)
	;inc rdi								; RDI++ (si actualizez pointerul la sir)
	;jmp @B								; reiau bucla WHILE (salt la @@ anterior/sus)
 ;@@:									; eticheta anonima pentru sfarsitul buclei WHILE


	;p3
	;xor ax, ax
	;lea rdi, string	
	
	;xor rcx, rcx
	;not rcx			;-1 (toti bitii pe 1)
	
	;cld				;DF = 0 ('\0' se afla la finalul cuv)

	;REPNE SCASB
	
	;not rcx			;strlen(string)+1
	;dec rcx			;strlen(string)

	;lea rdi, string
	;add rdi, rcx
	
	;start_loop:		
	;	dec rdi
	;	LOOP start_loop


	;p4
	;lea rdi, string
;@@:
    ;cmp byte ptr [rdi], 0
	;jz @F
	;	mov r10b, byte ptr [rdi]
	;	add r10b, 'A' - 'a'			
	;	mov [rdi], r10b
	;	inc rdi
	;jmp @B
;@@:
	
	;invoke puts, addr string
	
	
	;p5
	lea rdi, string
start:
    cmp byte ptr [rdi], 0
	jz final
		mov al, byte ptr [rdi]

		cmp al, 'a' ; daca AL este vocala 'a'
		je @F ; atunci sar la @@ de mai jos
		cmp al, 'e' ; daca AL este vocala 'e'
		je @F ; atunci sar la @@ de mai jos
		cmp al, 'i' ; daca AL este vocala 'i'
		je @F ; atunci sar la @@ de mai jos
		cmp al, 'o' ; daca AL este vocala 'o'
		je @F ; atunci sar la @@ de mai jos
		cmp al, 'u' ; daca AL este vocala 'u'
		je @F ; atunci sar la @@ de mai jos
		jmp TestConsoane ; altfel nu este vocala mica si sar la "TestConsoane"
	@@: ; atunci este vocala "minuscula"
		sub al, 'a'-'A' ; convertesc AL la majuscula
		mov [rdi], al ; actualizez caracterul in sir
		jmp @F ; apoi sar la @@ de mai jos peste testul de consoane
TestConsoane:
		cmp al, 'A' ; compar AL cu caracterul 'A'
		jb @F ; AL<'A' (comp. fara semn) => salt la @@ de mai jos
		cmp al, 'Z' ; compar AL cu caracterul 'Z'
		ja @F ; AL>'Z' (comp. fara semn) => salt la @@ de mai jos
		cmp al, 'A' ; daca AL este vocala 'A'
		je @F ; atunci sar la @@ de mai jos
		cmp al, 'E' ; daca AL este vocala 'E'
		je @F ; atunci sar la @@ de mai jos
		cmp al, 'I' ; daca AL este vocala 'I'
		je @F ; atunci sar la @@ de mai jos
		cmp al, 'O' ; daca AL este vocala 'O'
		je @F ; atunci sar la @@ de mai jos
		cmp al, 'U' ; daca AL este vocala 'U'
		je @F ; atunci sar la @@ de mai jos
		; altfel este majuscula consoana (diferita de vocala)
		add al, 'a'-'A' ; convertim caracterul din majuscula in "minuscula"
		mov [rdi], al ; actualizare caracterul modificat in sir
	@@:
		inc rdi ; RSI++ (actualizam pointerul la sir)
		jmp start ; salt la inceputul buclei WHILE
		final: ; eticheta de sfarsit a buclei WHILE
	
		invoke puts, addr string
	
	invoke ExitProcess, 0				; invoke ExitProcess API
	ret
	
	



 main endp
end