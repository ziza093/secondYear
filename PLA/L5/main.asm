include win64.inc
.code
 main proc
	 sub rsp, 8+32				; aliniere stiva 16o + alocare shadow area
	 mov rcx, -59				;CX <= paramentrul functiei PutU32
	 call PutI64				; apelul initial al functiei PutU32
	 xor rcx, rcx				; RCX <= paramentrul functiei ExitProcess
	 call ExitProcess			; apelul functiei ExitProcess (parasire program)
	 add rsp, 32+8				; eliberare spa?iu alocat pe stiva

	 ret
 main endp

 ;1-2)
 ;modificat si pentru cazul in care avem input: 0

 PutU32 proc					; argument => ECX (nr pe 32 biti de afisat)
	 sub rsp, 8+32				; variabila locala (align 8) + shadow area
	 mov eax, ecx				; EAX = ECX (argumentul functiei)
	 xor edx, edx				; EDX = 0 (partea high a deimpartitului)
	 mov ecx, 10				; ECX = 10 (impartitorul)
	 div ecx					; EDX:EAX / 10 => (EAX=cat, EDX = rest)

	 test eax, eax				; if (EAX != 0) // conditia de intrare in recursivitate
	 je @F						; { // echiv. cu (EAX == 0) cond. de iesire recursivitate		
		 mov [rsp+32], dl		; DL (rest) -> stiva (var. locala)
		 mov ecx, eax			; ECX <= parametru catre functia apelata recursiv
	 call PutU32				; apel recursiv
		 mov dl, [rsp+32]		; DL (rest) <- stiva (var locala)

	 @@:						; }
		 add dl, '0'			; conversie valoarea cifrei din CL in caracter ASCII
		 mov cl, dl				; CL <= parametru catre functia de afisare caracte
		 call putchar			; apelul functiei putchar (CL = caracterul de afisat)

	add rsp, 32+8				; eliberare spatiu alocat pe stiva
	ret							; revenire din procedura
 PutU32 endp


 ;3)
 
 PutU64 proc					; argument => ECX (nr pe 64 biti de afisat)
	 sub rsp, 8+32				; variabila locala (align 8) + shadow area
	 mov rax, rcx				; EAX = ECX (argumentul functiei)
	 xor rdx, rdx				; EDX = 0 (partea high a deimpartitului)
	 mov rcx, 10				; ECX = 10 (impartitorul)
	 div rcx					; EDX:EAX / 10 => (EAX=cat, EDX = rest)

	 test rax, rax				; if (EAX != 0) // conditia de intrare in recursivitate
	 je @F						; { // echiv. cu (EAX == 0) cond. de iesire recursivitate		
		 mov [rsp+32], dl		; DL (rest) -> stiva (var. locala)
		 mov rcx, rax			; ECX <= parametru catre functia apelata recursiv
	 call PutU64				; apel recursiv
		 mov dl, [rsp+32]		; DL (rest) <- stiva (var locala)

	 @@:						; }
		 add dl, '0'			; conversie valoarea cifrei din CL in caracter ASCII
		 mov cl, dl				; CL <= parametru catre functia de afisare caracte
		 call putchar			; apelul functiei putchar (CL = caracterul de afisat)

	add rsp, 32+8				; eliberare spatiu alocat pe stiva
	ret							; revenire din procedura
 PutU64 endp

 ;4)
 ;numere in hexa
 PutH64 proc

	 sub rsp, 8+32				; variabila locala (align 8) + shadow area
	 mov rax, rcx				; EAX = ECX (argumentul functiei)
	 xor rdx, rdx				; EDX = 0 (partea high a deimpartitului)
	 mov rcx, 16				; ECX = 16 (impartitorul); 16 pt ca vrem sa afisam nr in baza 16
	 div rcx					; EDX:EAX / 16 => (EAX=cat, EDX = rest)

	 test rax, rax				; if (EAX != 0) // conditia de intrare in recursivitate
	 je @F						; { // echiv. cu (EAX == 0) cond. de iesire recursivitate		
		 mov [rsp+32], dl		; DL (rest) -> stiva (var. locala)
		 mov rcx, rax			; ECX <= parametru catre functia apelata recursiv
	 call PutH64				; apel recursiv
		 mov dl, [rsp+32]		; DL (rest) <- stiva (var locala)

	 @@:						; }
		 mov r8b,dl				;copiere rest intr-un registru pentru a putea efectua tesetul saltului
		 add dl, '0'			;conversie valoarea restului din DL in caracter ASCII
		 cmp r8b, 9				;testare daca nr este format doar dintr-o cifra sau este caracter (A,B,C,D,E,F)
		 jng @F
			add dl, 'A'			;conversie litera (A,B,C,D,E,F) - baza 16 (impreuna cu acel -10)
			sub dl, 10			;putin pierdut aici, idk ('A' + 10)?
			sub dl, '0'			;se scade acel + '0' adunat fortat la inceput
		 @@:
		 mov cl, dl			; CL <= parametru catre functia de afisare caracte
		 call putchar			; apelul functiei putchar (CL = caracterul de afisat)

	add rsp, 32+8 ; eliberare spatiu alocat pe stiva
	ret ; revenire din procedura

 PutH64 endp



 ;5)
 ;afisare numere intregi cu semn 64biti
 PutI64 proc

	sub rsp, 8+32			; variabila locala (align 8) + shadow area
	mov rax, rcx			; EAX = ECX (argumentul functiei)
	 
	cmp rax, 0				;compara rax cu 0
	jge @F					;test daca nr este negativ
		mov cl, 45			;45 = '-' in ASCII
		mov [rsp+32], rax	;salveaza valoarea lui rax pe stiva (pt ca fiind volatil apelul fct putchar il poate modifica)
		call putchar		;apelul fct imi modifica rax ??? (afisare '-')
		mov rax, [rsp+32]	;extrage rax de pe stiva
		not rax				;compl fata de 2	
		inc rax				;pentru a salva modulul nr
	@@:
		mov rcx, rax		;pregatire parametru pentru apelul functiei
		call PutU64			;afisare numar in mod normal (numarul pozitiv)

	add rsp, 32+8			; eliberare spatiu alocat pe stiva
	ret						; revenire din procedura

 PutI64 endp

 ;6)
 ;7)



end