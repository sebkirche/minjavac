# Virtual table definitions:
segment .data
Factorial#vt: dd Factorial#main
Fac2#vt: dd Fac#ComputeFac, Fac2#method
Fac#vt: dd Fac#ComputeFac
# Method definitions:
segment .text


 _main:
 Factorial#main:
   push ebp
   mov ebp, esp
   sub esp, 2



   push edx
   call #new_Fac
   push edx
   push 10
   mov ebx, [ebp-8]
   mov edx, ebx
   call [edx + 0]
   add esp, 8
   pop edx
   push edx
   mov ebx, [ebp-4]
   push ebx
   call #print_int
   add esp, 4
   pop edx
   mov esp, ebp
   pop ebp
   ret
 Fac2#method:
   push ebp
   mov ebp, esp
   sub esp, 0



   mov [edx+4], 2
   mov eax, 4
   mov esp, ebp
   pop ebp
   ret
   mov esp, ebp
   pop ebp
   ret
 Fac#ComputeFac:
   push ebp
   mov ebp, esp
   sub esp, 8



   push edx
   push 3
   call #new_array
   add esp, 4
   pop edx
   mov ebx, [ebp-4]
   mov ecx, 0
   add ecx, 1
   mov ecx, [ebp-24]
   mov [ebx+4*ecx+1], 3
   mov ebx, [ebp+8]
   cmp ebx, 1
   jae .if_false



   mov ebx, 1
   jmp .if_next



 .if_false:
   mov ebx, [ebp+8]
   mov ecx, ebx
   add ecx, 0
   push edx
   mov ebx, [ebp+8]
   mov ecx, ebx
   sub ecx, 1
   mov ebx, [ebp-20]
   push ebx
   mov edx, edx
   call [edx + 0]
   add esp, 8
   pop edx
   mov ebx, [ebp-12]
   mov ecx, [ebp-8]
   mov eax, ebx
   imul eax, ecx
   mov ebx, [ebp-16]
   mov [ebp-28], ebx



 .if_next:
   mov ebx, [ebp-28]
   mov eax, ebx
   mov esp, ebp
   pop ebp
   ret
   mov esp, ebp
   pop ebp
   ret
