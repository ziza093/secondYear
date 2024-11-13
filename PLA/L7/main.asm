option casemap:none

extern putchar:proc ; parametru CL (caracter de afisat)

.code



;2)
 MaxVect proc

    sub rsp, 32
    
    ;rcx = primul parametru (v)
    ;rdx = al doilea parametru (len)
    
    test rcx,rcx
    jz final            ;v = NULL

    test rdx, rdx
    jz final            ;len = NULL

    mov ax, [rcx]       ;ax = v[0]
    dec rdx             ;decrementare len pt ca am extras elem din vector
                        ;pot muta len in ecx si schimba while cu LOOP
    start_loop:
        add rcx, 2      ;i++
        mov r8w, [rcx]  ;r8w = v[i] 
        cmp r8w, ax     ;compar element cu max din vector
        jna end_loop    ;sari daca elem e mai mare sau egal cu ax (r8d <= ax?)
        mov ax, r8w     ;r8w > ax 

    end_loop:           
        dec rdx          
        test rdx,rdx
        jnz start_loop 
    
    final:
    
    add rsp, 32
    ret

 MaxVect endp   



 ;3)
 MinVect proc

    sub rsp, 32
    
    ;rcx = v
    ;rdx = len
    test rcx,rcx
    jz final            ;v = NULL

    test rdx, rdx
    jz final            ;len = NULL

    mov eax, [rcx]      ;ax = v[0]
    dec rdx             ;decrementare len pt ca am extras elem din vector
                        ;pot muta len in ecx si schimba while cu LOOP
    start_loop:
        add rcx, 4      ;i++
        mov r8d, [rcx]  ;r8w = v[i] 
        cmp r8d, eax    ;compar element cu meax din vector
        jg end_loop     ;sari daca elem e mai mare sau egal cu eax (r8d > eax?)
        mov eax, r8d    ;eax = r8d 

    end_loop:           
        dec rdx          
        test rdx,rdx
        jnz start_loop 
    
    final:
    
    add rsp, 32
    ret

 MinVect endp 


 ;4)
 MaxVect64 proc

    sub rsp, 32
    ;rcx = v
    ;rdx = len
    test rcx,rcx
    jz final                ;v = NULL

    test rdx, rdx
    jz final                ;len = NULL

    ;lea r9d, [rcx]
    mov rax, [rcx]          ;ax = v[0]
    dec rdx                 ;decrementare len pt ca am extras elem din vector
                            ;pot muta len in ecx si schimba while cu LOOP
    start_loop:
        add rcx, 8          ;i++
        mov r8, [rcx]       ;r8w = v[i] 
        cmp r8, rax         ;compar element cu max din vector
        jl end_loop         ;sari daca elem e mai mare sau egal cu ax (r8d > ax?)
        mov rax, r8         ;rax = r8 

    end_loop:           
        dec rdx          
        test rdx,rdx
        jnz start_loop 
    
    final:
    
    add rsp, 32
    ret

 MaxVect64 endp



 ;5)
 NrAparitii proc

    sub rsp, 32

    ;primul parametru = RCX (sir de caractere)
    ;..2lea = RDX (caracter)

    xor rax, rax    ;rax  0

    test rdx, rdx   ;un parametru null
    jz final
    
    test rcx, rcx   ;un parametru null
    jz final

    ;bucla infinita

    start_loop:    
        mov r8b, [cl]   ;mov primul caracter
        test r8b, r8b   ;caracter null ('\0')
        jz final        ;conditie ieside din while indeplinita (atins caracter terminator de sir)
        cmp r8b,dl
        jne @F          ;caractere diferite
        inc rax         ;
        @@:
            inc cl
            jmp start_loop

    final:
    
    add rsp, 32
    ret


 NrAparitii endp

 ;6)
 ;algoritm sortare vector bubble sort
 ;4 1 3 8 2
 ;merge partial :))
 SortVector proc
   sub rsp, 32
    
    xor r8, r8                      ;r8 = 0
    xor r12, r12                    ;r12 = 0

    start_for1:
        mov r13, rdx                ;rdx--
        dec r13
        cmp r8, r13                 ;test for
        jge end_for1
        
        inc r8
        start_for2:
            mov r14, rdx            ;r14 = rdx-1-r8
            dec r14
            sub r14, r8             
            cmp r12, r14            ;test for
            jge end_for2

            mov r9, [rcx+r12*8]         ;get value v[j]
            mov r10, [rcx+r12*8+8]      ;get value v[j+1]
            lea rsi, [rcx+r12*8]        ;get adress of value v[j]
            lea rdi, [rcx+r12*8+8]      ;get adress of value v[j+1]

            cmp r9, r10                 ;compare v[j] with v[j+1]
            jle @F 
                ;swap
                ;
                mov [rsi], r10          ;la adresa primului nr se pune al 2 lea
                mov [rdi], r9           ;la adresa celui de-al doilea nr se pune primul
            @@:
            inc r12
            jmp start_for2
        
        end_for2:
    jmp start_for1
    end_for1:

    add rsp, 32
    ret

 SortVector endp

 AfisareVector proc

    

 AfisareVector endp

 PrintF proc
    mov  [rsp+8], rcx           ; copiere argumente pe stiva: (in SHADOW AREA)
    mov  [rsp+16], rdx
    mov  [rsp+24], r8
    mov  [rsp+32], r9
    sub  rsp, 24+32             ; alocare stiva: 3 REG protejati (24) + SHADOW AREA (32)
    mov  [rsp+32], rbx          ; salvare REG protejati utilizati
    mov  [rsp+40], rbp
    mov  [rsp+48], rsi

    lea  rbp, [rsp+72]          ; RBP = pointer catre arg2 (*arg2)
    mov  rsi, rcx               ; RSI = *sir = pointer catre sirul "format" (arg1)
    xor  ebx, ebx               ; (R)EBX = 0 (contor caractere afisate)

  start:
    mov  cl, [rsi]              ; CL = caracterul curent din sir "format"
    test cl, cl                
    jz   done                   ; conditie parasire bucla WHILE (*sir=='\0')
    cmp  cl, '%'                ; detectie caracater '%' in sir
    je   format                 ; daca *sir = '%' salt la eticheta "format
        call putchar            ; apel functie C "putchar" (parametru in CL)
        inc  ebx                ; contor++ (contor caractere din (R)EBX)
        jmp  continue
  format:
    inc  rsi                    ; *sir++
    mov  cl, [rsi]              ; urmatorul caracter din sir "format"
    test cl, cl                
    jz   done                   ; daca *sir=='\0' parasire bucla WHILE
    cmp  cl, '%'                ; identificare "%%" - afisare caracter '%' 
    jne  @F
    call putchar                ; apel functie C "putchar" (parametru in CL)
    inc  ebx                    ; contor++
    jmp  continue
  @@:
    cmp  cl, 'c'                ; identificare "%c" - afisare caracter
    jne  @F
    mov  cl, [rbp]              ; CL = parametrul de pe stiva:
    call putchar                ; apel functie C "putchar" (parametru in CL)
    add  rbp, 8                 ; actualizare pointer arg++ (urmatorul parametru pe stiva)
    inc  ebx                    ; contor++
    jmp  continue
  @@:   
    cmp  cl, 's'                ; identificare "%s" - afisare sir
    jne  @F
    mov  [rsp+64], rsi          ; salvare RSI in SHADOW AREA
    mov  rsi, [rbp]             ; rdi = pointer sir de pe stiva:
    add  rbp, 8                 ; actualizare pointer arg++ (urmatorul parametru pe stiva)
    jmp  @while_cond            ; fortare evaluare conditie DO-WHILE => WHILE
  @while_string:                 
        call putchar            ; apel functie C "putchar" (parametru in CL)
        inc  ebx                ; contor++
        inc  rsi                ; pointer sir++ (arg. "%s")
  @while_cond:
    mov  cl, [rsi]              ; CL = caracterul din arg. pentru "%s"
    test cl, cl                 ; conditie repetare WHILE pt afisare sir
    jnz  @while_string          ; *sir != '\0' (arg "%s")
    mov  rsi, [rsp+64]          ; restaurare valoare RSI din SHADOW AREA
    jmp  continue
  @@: 
    cmp  cl, 'u'                ; identificare "%u" - afisare uint16
    jne  @F
    movzx rcx, word ptr [rbp]   ; RCX = parametrul de pe stiva:
    call PutU64                 ; apel functie "PutU64"
    add  ebx, eax               ; actualiare contor
    add  rbp, 8                 ; *arg++
    jmp  continue
  @@:
    cmp  cl, 'd'                ; identificare "%d" afisare sint16
    jne  @F
    movsx rcx, word ptr [rbp]   ; RCX = parametrul de pe stiva:
    call PutI64                 ; apel functie "PutI64"
    add  ebx, eax               ; actualiare contor
    add  rbp, 8                 ; *arg++
    jmp  continue
  @@:
    cmp  cl, 'X'                ; identificare "%X" afisare hexa32
    jne  @F
    mov  ecx, dword ptr [rbp]   ; ECX = parametrul de pe stiva:
    mov  ecx, ecx               ; RCX = zero extend ECX
    call PutH64                 ; apel functie "PutI64"
    add  ebx, eax               ; actualiare contor
    add  rbp, 8                 ; *arg++
    jmp  continue
  @@:
    cmp  cl, 'l'                ; identificare "%l" 
    jne  continue
    inc  rsi                    ; *sir++
    mov  cl, [rsi]              ; urmatorul caracter din sir "format"
    test cl, cl                
    jz   done                   ; daca *sir=='\0' parasire bucla WHILE
    cmp  cl, 'u'                ; identificare "%lu" afisare uint32
    jne  @F
    mov  ecx, dword ptr [rbp]   ; ECX = parametrul de pe stiva:
    mov  ecx, ecx               ; RCX = zero extend ECX
    call PutU64                 ; apel functie "PutU64"
    add  ebx, eax               ; actualiare contor
    add  rbp, 8                 ; *arg++
    jmp  continue
  @@:
    cmp  cl, 'd'                ; identificare "%ld" afisare sint32
    jne  @F
    movsxd rcx, dword ptr [rbp] ; RCX = parametrul de pe stiva:
    call PutI64                 ; apel functie "PutI64"
    add  ebx, eax               ; actualiare contor
    add  rbp, 8                 ; *arg++
    jmp  continue
  @@:
    cmp  cl, 'X'                ; identificare "%lX" afisare hexa32
    jne  @F
    mov  ecx, dword ptr [rbp]   ; ECX = parametrul de pe stiva:
    mov  ecx, ecx               ; RCX = zero extend ECX
    call PutH64                 ; apel functie "PutI64"
    add  ebx, eax               ; actualiare contor
    add  rbp, 8                 ; *arg++
    jmp  continue
  @@:
    cmp  cl, 'l'                ; identificare "%ll" 
    jne  continue
    inc  rsi                    ; *sir++
    mov  cl, [rsi]              ; urmatorul caracter din sir "format"
    test cl, cl                
    jz   done                   ; daca *sir=='\0' parasire bucla WHILE
    cmp  cl, 'u'                ; identificare "%llu" afisare uint64
    jne  @F
    mov  rcx, qword ptr [rbp]   ; RCX = parametrul de pe stiva
    call PutU64                 ; apel functie C "PutU64"
    add  ebx, eax               ; actualiare contor
    add  rbp, 8                 ; *arg++
    jmp  continue
  @@:
    cmp  cl, 'd'               ; identificare "%lld" afisare sint64
    jne  @F
    mov  rcx, qword ptr [rbp]   ; RCX = parametrul de pe stiva
    call PutI64                 ; apel functie "PutI64"
    add  ebx, eax               ; actualiare contor
    add  rbp, 8                 ; *arg++
    jmp  continue
  @@:
    cmp  cl, 'X'                ; identificare "%llX" afisare hexa64
    jne  @F
    mov  rcx, qword ptr [rbp]   ; RCX = parametrul de pe stiva
    call PutH64                 ; apel functie C "PutI64"
    add  ebx, eax               ; actualiare contor
    add  rbp, 8                 ; *arg++
    jmp  continue
  @@:
; etc...
  continue:
    inc  rsi                    ; *sir++ (actualizare pointer sir)
    jmp  start                  ; revenire WHILE
  done:
    mov  eax, ebx               ; returnare contor caractere
    mov  rbx, [rsp+32]          ; restaurare REG protejati utilizati
    mov  rbp, [rsp+40] 
    mov  rsi, [rsp+48] 
    add  rsp, 32+24             ; eliberare spatiu alocat pe stiva
    ret
 PrintF endp

 PutU64 proc ; parametru: numar FARA SEMN in RCX
    sub  rsp, 8+32      ; alocare var locala (8) + Shadow Area (32)
    mov  [rsp+56], rbx  ; salvare reg protejat RBX in Shadow Area (slot 2/4)
    mov  rax, rcx       ; RAX = parametrul functiei din RCX
    xor  ebx, ebx       ; (R)EBX = 0 (contor caractere afisate)
    xor  rdx, rdx       ; RDX = 0
    mov  rcx, 10        ; RCX = 10 (baza de numeratie)
    div  rcx            ; RDX:RAX / 10  (RAX = cat, RDX = rest)
    test rax, rax       ; RAX == 0 (conditia de iesire)
    jz   @F             ; daca RAX == 0 salt la @@ de mai jos
        mov  [rsp+32], dl   ; DL (rest) -> stiva (variabila locala)
        mov  rcx, rax       ; parametrul functiei "PutU64"
        call PutU64         ; apel recursiv
        mov  rbx, rax       ; salvare val returnata
        mov  dl, [rsp+32]   ; DL <- stiva (variabila locala)
  @@:
    add  dl, '0'        ; conversie cifra din DL in caracter ASCII
    mov  cl, dl         ; parametrul pentru functia C "putchar"
    call putchar        ; apel functie C "putchar"
    inc  ebx            ; (R)EBX++ incrementare contor caractere
    mov  eax, ebx       ; (R)EAX = (R)EBX = valoare returnata 
    mov  rbx, [rsp+56]  ; restaurare reg protejat RBX
    add  rsp, 32+8      ; eliberare spatiu alocat pe stiva
    ret
 PutU64 EndP

 PutH64 proc ; parametru: numar FARA SEMN in RCX
    sub  rsp, 8+32      ; alocare var locala (8) + SHADOW AREA (32)
    mov  [rsp+56], rbx  ; salvare registru protejat
    xor  ebx, ebx       ; (R)EBX = 0 (contor caractere afisate)
    mov  rax, rcx       ; RAX = parametrul functiei din RCX
    shr  rax, 4         ; RAX >>= 4 echivalent RAX = RAX/16
    mov  dl, cl         ; DL = octetul cel mai nesemnificativ din nr.
    and  dl, 0Fh        ; DL&=0xF => DL = hex-cifra din "stanga" nr.
    test rax, rax       ; RAX == 0 (conditia de iesire)
    jz   @F             ; daca RAX == 0 salt la @@ de mai jos
        mov  [rsp+32], dl   ; DL (rest) -> stiva (variabila locala)
        mov  rcx, rax       ; parametrul functiei "PutH64"
        call PutH64         ; apel recursiv
        mov  rbx, rax       ; salvare val returnata
        mov  dl, [rsp+32]   ; DL <- stiva (variabila locala)
  @@:
    add  dl, '0'        ; conversie cifra din DL in caracter ASCII
    cmp  dl, '9'        ; comparare DL cu caracterul '9' 
    jna  @F             ; daca este cifra zecimala [0..9] salt la @@
        add dl, 'A'-'9'-1    ; compensare pentru conversie cifra hexa [A..F]
  @@:
    mov  cl, dl         ; parametrul pentru functia C "putchar"
    call putchar        ; apel functie C "putchar"
    inc  ebx            ; EBX++ incrementare contor caractere
    mov  eax, ebx       ; (R)EAX = (R)EBX = valoare returnata 
    mov  rbx, [rsp+56]  ; refacere valoare RBX (reg protejat)
    add  rsp, 32+8      ; eliberare spatiu alocat pe stiva
    ret
 PutH64 EndP

 PutI64 proc ; parametru: numar CU SEMN in RCX
    sub  rsp, 8+32      ; alocare var locala (8) + SHADOW AREA (32)
    mov  [rsp+48], rcx  ; salvare RCX in SHADOW AREA
    mov  [rsp+32], rbx  ; salvare registru protejat
    xor  ebx, ebx       ; (R)EBX = 0 (contor caractere afisate)
    test rcx, rcx       ; verificare valoare RCX
    jns  @F             ; daca este numar pozitiv salt la @@ de mai jos
        mov  cl, '-'        ; parametru pentru functia "putchar"
        call putchar        ; apel functie C "putchar"
        mov rcx, [rsp+48]   ; refacere valoare RCX (parametrul functiei)
        neg rcx             ; RCX = -RCX = valoare obsoluta RCX
        inc ebx             ; incrementare contor caractere
  @@:
    call PutU64         ; apel PutU64 (in RAX returneaza nr de caractere afisate)
    add  eax, ebx       ; (R)EAX += (R)EBX returnare contor caractere
  @@:
    mov  rbx, [rsp+32]  ; restaurare registru protejat
    add  rsp, 32+8      ; eliberare spatiu alocat pe stiva
    ret    
 PutI64 EndP

 PutP64 proc ; parametru: numar intreg FARA SEMN in RCX
	sub  rsp, 8+32				; alocare 1 "var locala" + Shadow area
	mov  [rsp+32], rbx			; salvare reg. protejat RBX (in var locala)
	mov  [rsp+56], rdi          ; salvare reg. protejat EDI (in Shadow Area slot 2/4)
	mov  rbx, rcx				; incarcare in RBX a paramentrului din RAX
	mov  di, 16					; contor nibbles (nr pe 64 biti = 16 hex-cifre)
  PutP64_repeat:                ; inceput bucla REPEAT-UNTIL
        rol  rbx, 4             ;    rotire RBX cu o hex-cifra la stanga
        mov  cl, bl             ;    CL = octetul cel mai semnificativ din RBX
		and  cl, 0Fh            ;    CL = doar hex-cifra cea mai nesennificativa
        add  cl, '0'			;    conversie cifra din CL in caracter ASCII
		cmp  cl, '9'			;    comparare CL cu caracterul '9'
		jna  @F					;    daca este cifra zecimala ['0'..'9'] salt la @@
			add cl, 'A'-'9'-1	;       altfel compensare pt hex-ciferele 'A'..'F'
      @@:
		call putchar			; afisare caracter
		dec  di					; decrementare contor DI
    jnz  PutP64_repeat		    ; daca DI != 0 salt la inceputul buclei REPEAT-UNTIL
	mov  eax, 16				; returnare nr. de caractere afisate
	mov  rbx, [rsp+32]			; refacere valoare registrul protejat RBX
	mov  rdi, [rsp+56]          ; refacere valoare registrul protejat RDI
    add  rsp, 32+8              ; eliberare spatiu alocat pe stiva 
	ret
 PutP64 EndP

end