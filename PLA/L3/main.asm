include win64.inc                ; biblioteca cu structuri si functii Win x64

.data                            ; segmentul de date (variabile)
	
	;p1	
	am qword	17
	bm qword 23
	cm qword 11
	

	;p2
	VAR dword 110011b

	;p3
	var dword 17,31,25,20

	;p4
	vector dword -10, -15, -20, -25, -30, -9

	;p5
	vector dword 1,2,3,4,5,6,7,9

	;p6
	vector dword 10, 20, 30, 40, 50

.code                            ; Sergmentul de cod (program)	

main proc                        ; functia principala "main" 
	
	;p1
	mov rax, am
	mov rcx, bm
	cmp rax, rcx		
	jae @F				;a > b
		mov rax, rcx	;a < b, deci a = b
	@@: 
	mov rcx, cm
	cmp rax, rcx
	jg @F
		mov rax, rcx
	@@:
	;maximul este in registrlu RAX


	;p2
	mov eax, VAR			;eax = var = 50h = 110011b
	xor ebx, ebx			;ebx = 0
	@@:
		shr eax, 1			;eax = eax/2 = 11001b si CF = 1/0 (cand shiftez, bitul shiftat se duce in CF)
		adc ebx, 0			;ebx = ebx + CF + 0
	test eax, eax			;EAX - EAX == 0? (se modifica ZF, etc)
	jnz @B					;daca eax == 0 jump la @@:


	;p3
	xor bl, bl	
	lea rax, var			;17,31,25,20
	l1:
		cmp rax, sizeof(var)
		jnl 
		mov edx, [rax]
		test edx, 1
		
		jnz @F
		inc bl
		
		@@:
			add rax, sizeof(dword)
			LOOP l1



	;p4 
	lea rax, vector
	mov ecx, 5
	mov ebx, [rax]
	
	start_loop:
		mov edx, [rax + sizeof(dword)]
		cmp ebx, edx
		ja @F
			mov ebx, edx
		@@:
			add rax, sizeof(dword)
	LOOP start_loop

	
	;p5
	mov ecx, 4		;daca am nr impar de elem nu merge, dar pot adauga dupa cond sa vad
	mov r8d, ecx	;
	shr r8d, 1		;am cate n/2 grupuri de medii aritmetice de 2 nr: a,b,c,d,e,f,g,h = ((a+b)/2 + (c+d)/2 + (e+f)/2+...)/4

	lea rax, vector
	xor r9d, r9d

	start_loop:
		mov ebx, [rax]
		mov edx, [rax + sizeof(dword)]		;fac ma a 2 nr
		adc ebx, edx						;
		sar ebx, 1							;

		add r9d, ebx						;adun mediile segmentate

		add rax, sizeof(dword)				;sar cate 2 pozitii
		add rax, sizeof(dword)
	LOOP start_loop

	sar	r9d, 2								;impart la n/2 rezultatule mediile segmentate
	

	;p6
	lea rsi, vector
	lea rdi, [rsi + 4*4]

	reverse_loop:
		cmp rsi, rdi
		jge done
	
		mov eax, [rsi]
		mov ebx, [rdi]
		mov [rsi], ebx
		mov [rdi], eax

		add rsi, 4
		sub rdi, 4

		jmp reverse_loop

	done:

	lea rax, vector
	mov ecx, 5
	@@:
		mov rbx, [rax]						 
		add rax, sizeof(dword)
	LOOP @B

	call ExitProcess             ; parasirea programului
	ret
main endp                        ; sfarsitul functiei main

 ; alte functii/proceduri

end