include win64.inc
option win64:0111b
option literals:on

MinandMax proto (BOOL) :ptr, :qword, :ptr, :ptr 
Appearances proto (DWORD) :ptr, :byte

.data
	sir byte "mamasitata", 0				;char array declaration
	char byte "t"
	vect dword 1, 2, 3, 4, 5				;array declaration
	min dword ?								;min declaration
	max dword ?								;max declaration
.code


main proc

	;MinandMax(&vect, LENGTHOF(vect), &min, &max) 
	;printf("The minimum and maximum are: %ld %ld\n", min, max) 

	printf("The number off appearances off the char: '%c' is: %ld\n", char, Appearances(&sir, char))

	exit(0)
	ret

main endp

;It only works for unsigned numbers
MinandMax proc uses rbx rsi rdi @vect:ptr, vectLength:qword, @min:ptr, @max:ptr

	.if @vect && vectLength && @min && @max 
		mov rbx, @vect						;rbx = vect
		mov esi, [rbx]						;rsi = min(first elem from the array)
		mov edi, [rbx]						;rdi = max(same)
		mov rcx, vectLength					;rcx = lengthof(vect)

		dec rcx								;if the array has 5 elements we will run 5 times, but the while stops at 0, so we dec rbx

		.while rcx							;while rcx != 0
		
			add rbx, 4						;go to the next element in the array
			mov eax, [rbx]					;get that element


			.if esi > eax					;compare the elements (the current one and the first one)
				mov esi, eax
			.endif

			.if edi < eax					;same here, but with different test condition
				mov edi, eax
			.endif

			dec rcx							;rcx--
		.endw

		mov rbx, @min						;rbx = &min
		mov [rbx], esi						;&min = (the minimum)

		mov rbx, @max						;same here (rbx = &max)
		mov [rbx], edi						;&max = (the maximum)

		mov rax, 1						;return 1 (true)
	.else
		mov rax, 0						;return 0 (return false)
	.endif

	ret									
MinandMax endp

Appearances proc uses rbx @sir:ptr, @char:byte	 

	.if @sir && @char
		mov rbx, @sir					;get the adress of the array
		mov sil, @char					;get the char we need to count the appearances for

		mov al, [rbx]					;get the first char from the array

		xor rcx, rcx					;initialize rcx = 0

		.while al						;while (al != '\0')

			.if sil == al				;if the chars correspond
				inc rcx					;increment the appearances counter
			.endif
			
			inc bx						;got to the next char in the array
			mov al, [rbx]				;get that char
		.endw
		
		mov rax, rcx					;return the number of times the char is in the char array
	.else
		mov rax, -1						;something went wrong
	.endif

	ret
Appearances endp





end