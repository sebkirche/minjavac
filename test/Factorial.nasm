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
   pusha
   push dword 4
   call _alloc
   add esp, 4
   mov [eax+0], dword Factorial@@vt
   popa
   ret

 Fac@@new:
   pusha
   push dword 4
   call _alloc
   add esp, 4
   mov [eax+0], dword Fac@@vt
   popa
   ret


; code
segment .text

 _main:
 Factorial@main:
   push ebp
   mov ebp, esp
   sub esp, 12

   push edx                    ; save_context
   ; eax dest of .new_Fac
   call [edx+-4]               ; .new_Fac := call Fac@@new
   pop edx                     ; load_context
   push edx                    ; save_context
   push 10                     ; param 10
   ; eax source of .new_Fac
   mov edx, eax                ; param .new_Fac
   ; dead .new_Fac
   ; eax dest of .call
   call [edx+0]                ; .call := call Fac@ComputeFac
   add esp, 4                  ; load_context
   pop edx                     ; load_context
   pusha                       ; save_c_context
   ; eax source of .call
   push eax                    ; param .call
   ; dead .call
   ; eax dest of .void
   call _print_int             ; .void := call _print_int
   add esp, 4                  ; load_c_context
   popa                        ; load_c_context

   mov esp, ebp
   pop ebp
   ret


 Fac@ComputeFac:
   push ebp
   mov ebp, esp
   sub esp, 60

   mov ebx, 0                  ; i := 0
   ; ebx source of i
   ; ecx dest of .add
   mov ecx, ebx                ; .add := add i, 1
   add ecx, 1                  ; .add := add i, 1
   ; ecx source of .add
   ; spill j for exit
   mov [ebp-12], ecx
   ; spill i for exit
   mov [ebp-16], ebx

 .loop:
   ; ebx source of i
   mov ebx, [ebp-16]
   ; ecx source of num
   mov ecx, [ebp+8]
   cmp ebx, ecx                ; if greater_or_equal(i, num) goto .while_false
   jae .while_false            ; if greater_or_equal(i, num) goto .while_false

   pusha                       ; save_c_context
   ; ebx source of i
   mov ebx, [ebp-16]
   push ebx                    ; param i
   ; eax dest of .void
   call _print_int             ; .void := call _print_int
   add esp, 4                  ; load_c_context
   popa                        ; load_c_context
   ; ebx source of j
   mov ebx, [ebp-12]
   ; ecx dest of .mult
   mov ecx, 2                  ; .mult := mult 2, j
   imul ecx, ebx               ; .mult := mult 2, j
   ; eax source of i
   ; dead .void
   mov eax, [ebp-16]
   ; ecx source of .mult
   ; ebx dest of .add_D
   mov ebx, eax                ; .add_D := add i, .mult
   add ebx, ecx                ; .add_D := add i, .mult
   ; ebx source of .add_D
   ; ecx dest of .add_C
   ; dead .mult
   mov ecx, ebx                ; .add_C := add .add_D, 1
   add ecx, 1                  ; .add_C := add .add_D, 1
   ; ecx source of .add_C
   ; ebx dest of .sub_A
   ; dead .add_D
   mov ebx, ecx                ; .sub_A := sub .add_C, 4
   sub ebx, 4                  ; .sub_A := sub .add_C, 4
   ; ebx source of .sub_A
   ; ecx dest of .add_B
   ; dead .add_C
   mov ecx, ebx                ; .add_B := add .sub_A, 4
   add ecx, 4                  ; .add_B := add .sub_A, 4
   ; ecx source of .add_B
   ; ebx dest of .sub
   ; dead .sub_A
   mov ebx, ecx                ; .sub := sub .add_B, 3
   sub ebx, 3                  ; .sub := sub .add_B, 3
   ; ebx source of .sub
   ; ecx source of j
   ; dead .add_B
   mov ecx, [ebp-12]
   ; eax dest of .add_A
   ; dead i
   mov eax, ebx                ; .add_A := add .sub, j
   add eax, ecx                ; .add_A := add .sub, j
   ; eax source of .add_A
   ; ecx source of j
   ; eax source of i
   ; ebx dest of .add_F
   ; dead .sub
   mov ebx, ecx                ; .add_F := add j, i
   add ebx, eax                ; .add_F := add j, i
   ; ebx source of .add_F
   ; ecx dest of .add_E
   ; dead j
   mov ecx, ebx                ; .add_E := add .add_F, 5
   add ecx, 5                  ; .add_E := add .add_F, 5
   ; eax source of i
   ; ebx dest of .mult_A
   ; dead .add_F
   mov ebx, eax                ; .mult_A := mult i, 7
   imul ebx, 7                 ; .mult_A := mult i, 7
   ; ecx source of .add_E
   ; ebx source of .mult_A
   ; eax dest of .sub_B
   ; spill i: [eax], onMem = false
   mov [ebp-16], eax
   mov eax, ecx                ; .sub_B := sub .add_E, .mult_A
   sub eax, ebx                ; .sub_B := sub .add_E, .mult_A
   ; eax source of .sub_B
   ; spill j for exit
   mov [ebp-12], eax
   jmp .loop                   ; goto .loop

 .while_false:
   ; ebx source of num
   mov ebx, [ebp+8]
   mov eax, ebx                ; return num
   mov esp, ebp                ; return num
   pop ebp                     ; return num
   ret                         ; return num


