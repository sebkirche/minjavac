class Factorial:

procedure Factorial@main
   ; Block     : 0
   ; adj       : []
   ; write     : [.call, .new_Fac]
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
   .call := call _print_int;
   load_context;
end
end

class Fac:

procedure Fac@ComputeFac
   ; Block     : 0
   ; adj       : []
   ; write     : []
   ; read      : [num]
   ; firstRead : [num]
   ; live      : []
   return num;
end
end