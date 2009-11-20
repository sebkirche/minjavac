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
   push dword 4
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
   push 10                     ; param 10
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
   sub esp, 12

   mov ebx, 0                  ; i := 0
   ; spill i for exit
   mov [ebp-8], ebx

 .loop:
   ; ebx source of i
   mov ebx, [ebp-8]
   ; ecx source of num
   mov ecx, [ebp+8]
   cmp ebx, ecx                ; if greater_or_equal(i, num) goto .while_false
   jae .while_false            ; if greater_or_equal(i, num) goto .while_false

   push edx                    ; save_c_context
   ; ebx source of i
   mov ebx, [ebp-8]
   push ebx                    ; param i
   ; eax dest of .void
   call _print_int             ; .void := call _print_int
   add esp, 4                  ; load_c_context
   pop edx                     ; load_c_context
   ; ebx source of i
   mov ebx, [ebp-8]
   ; ecx dest of .add
   mov ecx, ebx                ; .add := add i, 1
   add ecx, 1                  ; .add := add i, 1
   ; ecx source of .add
   ; spill i for exit
   mov [ebp-8], ecx
   jmp .loop                   ; goto .loop

 .while_false:
   ; ebx source of num
   mov ebx, [ebp+8]
   mov eax, ebx                ; return num
   mov esp, ebp                ; return num
   pop ebp                     ; return num
   ret                         ; return num


