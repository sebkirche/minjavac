; Factorial.nasm

; vt definitions
segment .data
 Factorial@@vt   : dd Factorial@main
 Fac@@vt         : dd Fac@ComputeFac


; code
segment .text

 _main:
 Factorial@main:
   push ebp
   mov ebp, esp
   sub esp, 12

   push edx                    ; save_context
   ; eax dest of .new_Fac
   call _new_Fac               ; .new_Fac := call _new_Fac
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
   push edx                    ; save_context
   ; eax source of .call
   push eax                    ; param .call
   ; dead .call
   ; eax dest of .void
   call _print_int             ; .void := call _print_int
   add esp, 4                  ; load_context
   pop edx                     ; load_context

   mov esp, ebp
   pop ebp
   ret


 Fac@ComputeFac:
   push ebp
   mov ebp, esp
   sub esp, 76

   mov ebx, 0                  ; i := 0
   ; ebx source of i
   ; ecx dest of .add
   mov ecx, ebx                ; .add := add i, 1
   add ecx, 1                  ; .add := add i, 1
   ; ecx source of .add
   mov ebx, 1                  ; q := 1
   mov eax, 2                  ; k := 2
   ; spill q for exit
   mov [ebp-56], ebx
   ; spill j for exit
   mov [ebp-20], ecx
   ; spill k for exit
   mov [ebp-24], eax

 .loop:
   ; ebx source of k
   mov ebx, [ebp-24]
   cmp 0, ebx                  ; if greater_or_equal(0, k) goto .while_false
   jae .while_false            ; if greater_or_equal(0, k) goto .while_false

   ; ebx source of j
   mov ebx, [ebp-20]
   ; ecx source of q
   mov ecx, [ebp-56]
   ; eax dest of .mult
   mov eax, ebx                ; .mult := mult j, q
   imul eax, ecx               ; .mult := mult j, q
   ; eax source of .mult
   ; ebx source of k
   mov ebx, [ebp-24]
   ; ecx dest of .sub
   mov ecx, eax                ; .sub := sub .mult, k
   sub ecx, ebx                ; .sub := sub .mult, k
   ; ecx source of .sub
   ; eax dest of .add_A
   ; dead .mult
   mov eax, ecx                ; .add_A := add .sub, 5
   add eax, 5                  ; .add_A := add .sub, 5
   ; eax source of .add_A
   ; ecx source of q
   ; dead .sub
   mov ecx, [ebp-56]
   ; ebx source of k
   ; eax dest of .add_D
   ; dead .add_A
   ; spill [eax], onMem = false
   mov [ebp-28], eax
   mov eax, ecx                ; .add_D := add q, k
   add eax, ebx                ; .add_D := add q, k
   ; eax source of .add_D
   ; ebx dest of .add_C
   mov ebx, eax                ; .add_C := add .add_D, 5
   add ebx, 5                  ; .add_C := add .add_D, 5
   ; ebx source of .add_C
   ; eax dest of .add_B
   ; dead .add_D
   mov eax, ebx                ; .add_B := add .add_C, 4
   add eax, 4                  ; .add_B := add .add_C, 4
   ; ebx source of j
   ; dead .add_C
   mov ebx, [ebp-20]
   ; ecx source of i
   mov ecx, [ebp-28]
   ; eax dest of .mult_C
   ; spill [eax], onMem = false
   mov [ebp-36], eax
   mov eax, ebx                ; .mult_C := mult j, i
   imul eax, ecx               ; .mult_C := mult j, i
   ; eax source of .mult_C
   ; ebx source of k
   mov ebx, [ebp-24]
   ; ecx dest of .mult_B
   mov ecx, eax                ; .mult_B := mult .mult_C, k
   imul ecx, ebx               ; .mult_B := mult .mult_C, k
   ; ecx source of .mult_B
   ; eax source of q
   ; dead .mult_C
   mov eax, [ebp-56]
   ; ebx dest of .mult_A
   mov ebx, ecx                ; .mult_A := mult .mult_B, q
   imul ebx, eax               ; .mult_A := mult .mult_B, q
   ; ecx source of .add_B
   ; dead .mult_B
   mov ecx, [ebp-36]
   ; ebx source of .mult_A
   ; eax dest of .sub_A
   ; dead q
   mov eax, ecx                ; .sub_A := sub .add_B, .mult_A
   sub eax, ebx                ; .sub_A := sub .add_B, .mult_A
   ; eax source of .sub_A
   ; eax source of q
   ; ebx source of j
   ; dead .mult_A
   mov ebx, [ebp-20]
   ; ecx dest of .add_F
   ; dead .add_B
   mov ecx, eax                ; .add_F := add q, j
   add ecx, ebx                ; .add_F := add q, j
   ; ecx source of .add_F
   ; ebx source of i
   ; dead j
   mov ebx, [ebp-28]
   ; eax dest of .add_E
   ; spill [eax], onMem = false
   mov [ebp-56], eax
   mov eax, ecx                ; .add_E := add .add_F, i
   add eax, ebx                ; .add_E := add .add_F, i
   ; eax source of .add_E
   ; ebx source of k
   ; dead i
   mov ebx, [ebp-24]
   ; ecx dest of .sub_B
   ; dead .add_F
   mov ecx, eax                ; .sub_B := sub .add_E, k
   sub ecx, ebx                ; .sub_B := sub .add_E, k
   ; ecx source of .sub_B
   ; ebx dest of .mult_D
   ; dead k
   mov ebx, ecx                ; .mult_D := mult .sub_B, 3
   imul ebx, 3                 ; .mult_D := mult .sub_B, 3
   ; ebx source of .mult_D
   ; spill k for exit
   mov [ebp-24], ebx
   jmp .loop                   ; goto .loop

 .while_false:
   ; ebx source of num
   mov ebx, [ebp+8]
   mov eax, ebx                ; return num
   mov esp, ebp                ; return num
   pop ebp                     ; return num
   ret                         ; return num


