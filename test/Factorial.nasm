; Factorial.nasm

; c runtime
extern _alloc, _new_array, _print_int


; vt definitions
segment .data
 Factorial@@vt   : dd Factorial@main
 Fac@@vt         : dd Fac@ComputeFac


; constructors
segment .text

 Factorial@@new:
   push dword 4
   call _alloc
   add esp, 4
   mov [eax+0], dword Factorial@@vt
   ret

 Fac@@new:
   push dword 8
   call _alloc
   add esp, 4
   mov [eax+0], dword Fac@@vt
   ret


; code
segment .text
 global _asmMain

 _asmMain:
 Factorial@main:
   push ebp
   mov ebp, esp
   sub esp, 12

   push edx                    ; save_context
   ; eax dest of .new_Fac
   call Fac@@new               ; .new_Fac := call Fac@@new
   pop edx                     ; load_context
   push edx                    ; save_context
   push 5                      ; param 5
   ; eax source of .new_Fac
   mov edx, eax                ; param .new_Fac
   ; dead .new_Fac
   ; eax dest of .call
   mov esi, [edx+0]            ; .call := call Fac@ComputeFac
   call dword [esi]            ; .call := call Fac@ComputeFac
   add esp, 4                  ; load_context
   pop edx                     ; load_context
   push edx                    ; save_c_context
   ; eax source of .call
   push eax                    ; param .call
   ; dead .call
   ; eax dest of .void
   call _print_int             ; .void := call _print_int
   add esp, 4                  ; load_c_context
   pop edx                     ; load_c_context

   mov esp, ebp
   pop ebp
   ret


 Fac@ComputeFac:
   push ebp
   mov ebp, esp
   sub esp, 40

   push edx                    ; save_c_context
   push 3                      ; param 3
   ; eax dest of .new_array
   call _new_array             ; .new_array := call _new_array
   add esp, 4                  ; load_c_context
   pop edx                     ; load_c_context
   ; eax source of .new_array
   ; ebx dest of .add
   mov ebx, 0                  ; .add := add 0, 1
   add ebx, 1                  ; .add := add 0, 1
   ; eax source of x
   ; ebx source of .add
   mov dword [eax+4*ebx+4], 3  ; x[.add] := 3
   ; eax source of x
   ; ebx dest of .add_A
   ; dead .add
   mov ebx, 0                  ; .add_A := add 0, 2
   add ebx, 2                  ; .add_A := add 0, 2
   mov ecx, [edx+4]            ; .facNum := facNum
   ; ecx source of .facNum
   ; eax source of y
   ; ebx source of .add_A
   mov dword [eax+4*ebx+4], ecx ; y[.add_A] := .facNum
   ; ebx source of num
   ; dead .add_A
   mov ebx, [ebp+8]
   cmp ebx, 1                  ; if greater_or_equal(num, 1) goto .if_false
   jae .if_false               ; if greater_or_equal(num, 1) goto .if_false

   mov ebx, 1                  ; num_aux := 1
   ; spill num_aux for exit
   mov [ebp-32], ebx
   jmp .if_next                ; goto .if_next

 .if_false:
   push edx                    ; save_context
   ; ebx source of num
   mov ebx, [ebp+8]
   ; ecx dest of .sub
   mov ecx, ebx                ; .sub := sub num, 1
   sub ecx, 1                  ; .sub := sub num, 1
   ; ecx source of .sub
   push ecx                    ; param .sub
   mov edx, edx                ; param this
   ; dead .sub
   ; eax dest of .call
   mov esi, [edx+0]            ; .call := call Fac@ComputeFac
   call dword [esi]            ; .call := call Fac@ComputeFac
   add esp, 4                  ; load_context
   pop edx                     ; load_context
   ; ebx source of num
   mov ebx, [ebp+8]
   ; eax source of .call
   ; ecx dest of .mult
   mov ecx, ebx                ; .mult := mult num, .call
   imul ecx, eax               ; .mult := mult num, .call
   ; ecx source of .mult
   ; spill num_aux for exit
   mov [ebp-32], ecx

 .if_next:
   ; ebx source of num_aux
   mov ebx, [ebp-32]
   mov eax, ebx                ; return num_aux
   mov esp, ebp                ; return num_aux
   pop ebp                     ; return num_aux
   ret                         ; return num_aux


