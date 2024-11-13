include win64.inc
.data
	string db "mother", 0 ; declarare sir de caractere
	;p5
	maskor db 00010000b			;pt a face litere mari
	maskand db 11101111b		;pt a face litere mici
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

	;afisare cu puchar?


	;p4
	lea rdi, string
@@:
    cmp byte ptr [rdi], 0
	jz @F
		mov r10b, byte ptr [rdi]
		or r10b, maskor
		inc rdi
	jmp @B
@@:
	
	;afisare
	lea rdi, string
	cmp byte ptr [rdi], 0
@@:
	jz @F
		inc rdi
	jmp @B
@@:

	invoke ExitProcess, 0				; invoke ExitProcess API
 ret									; }
 main endp
end