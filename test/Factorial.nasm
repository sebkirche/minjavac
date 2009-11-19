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
   sub esp, 8

   push edx               ; save_context
   call _new_Fac          ; .new_Fac := call _new_Fac
   pop edx                ; load_context
   push edx               ; save_context
   push 10                ; param 10
   mov edx, eax           ; param .new_Fac
   ; dead : .new_Fac
   call [edx+0]           ; .call := call Fac@ComputeFac
   add esp, 4             ; load_context
   pop edx                ; load_context
   push edx               ; save_context
   push eax               ; param .call
   ; dead : .call
   call _print_int        ; .call := call _print_int
   add esp, 4             ; load_context
   pop edx                ; load_context

   mov esp, ebp
   pop ebp
   ret


 Fac@ComputeFac:
   push ebp
   mov ebp, esp

   mov ebx, [ebp+8]
   mov eax, ebx           ; return num
   mov esp, ebp           ; return num
   pop ebp                ; return num
   ret                    ; return num


