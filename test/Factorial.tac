class Factorial:

procedure Factorial@main
   ; Block     : 0
   ; adj       : []
   ; write     : [.call, .void, .new_Fac]
   ; read      : [.call, .new_Fac]
   ; firstRead : []
   ; live      : []
   save_context;
   .new_Fac := call _new_Fac;
   load_context;
   save_context;
   param 10;
   param .new_Fac;
   .call := call Fac@ComputeFac;
   load_context;
   save_context;
   param .call;
   .void := call _print_int;
   load_context;
end
end

class Fac:

procedure Fac@ComputeFac
   ; Block     : 0
   ; adj       : [1]
   ; write     : [i]
   ; read      : []
   ; firstRead : []
   ; live      : [i]
   i := 0;

   ; Block     : 1
   ; adj       : [3, 2]
   ; write     : []
   ; read      : [num, i]
   ; firstRead : [num, i]
   ; live      : []
 .loop:
   if greater_or_equal(i, num) goto .while_false;

   ; Block     : 2
   ; adj       : [1]
   ; write     : [.void, .add, i]
   ; read      : [.add, i]
   ; firstRead : [i]
   ; live      : [i]
   save_context;
   param i;
   .void := call _print_int;
   load_context;
   .add := add i, 1;
   i := .add;
   goto .loop;

   ; Block     : 3
   ; adj       : []
   ; write     : []
   ; read      : [num]
   ; firstRead : [num]
   ; live      : []
 .while_false:
   return num;
end
end