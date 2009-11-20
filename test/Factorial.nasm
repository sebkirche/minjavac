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

   push edx               ; save_context
   call _new_Fac          ; .new_Fac := call _new_Fac
   pop edx                ; load_context
   push edx               ; save_context
   push 10                ; param 10
   mov edx, eax           ; param .new_Fac
   ; dead : .new_Fac
   ; removing .new_Fac from eax
   call [edx+0]           ; .call := call Fac@ComputeFac
   add esp, 4             ; load_context
   pop edx                ; load_context
   push edx               ; save_context
   push eax               ; param .call
   ; dead : .call
   ; removing .call from eax
   call _print_int        ; .void := call _print_int
   add esp, 4             ; load_context
   pop edx                ; load_context

   mov esp, ebp
   pop ebp
   ret


 Fac@ComputeFac:
   push ebp
   mov ebp, esp
   sub esp, 12

   mov ebx, 0             ; i := 0
   mov [ebp-8], ebx

 .loop:
   mov ebx, [ebp-8]
   mov ecx, [ebp+8]
   cmp ebx, ecx           ; if greater_or_equal(i, num) goto .while_false
   jae .while_false       ; if greater_or_equal(i, num) goto .while_false

   push edx               ; save_context
   mov ebx, [ebp-8]
   push ebx               ; param i
   ; removing i from ebx
   call _print_int        ; .void := call _print_int
   add esp, 4             ; load_context
   pop edx                ; load_context
   mov ebx, [ebp-8]
   mov ecx, ebx           ; .add := add i, 1
   add ecx, 1             ; .add := add i, 1
   mov [ebp-8], ecx
   jmp .loop              ; goto .loop

 .while_false:
   mov ebx, [ebp+8]
   mov eax, ebx           ; return num
   mov esp, ebp           ; return num
   pop ebp                ; return num
   ret                    ; return num


