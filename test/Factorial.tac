class Factorial:

procedure Factorial#main
   # Block     : 0
   # adj       : []
   # write     : [.call, .new_Fac]
   # read      : [.call]
   # firstRead : []
   # live      : []
   save_context;
   .new_Fac := call #new_Fac;
   save_context;
   param 10;
   param .new_Fac;
   .call := call Fac#ComputeFac;
   load_context;
   save_context;
   param .call;
   .call := call #print_int;
   load_context;
end
end

class Fac:

procedure Fac#ComputeFac
   # Block     : 0
   # adj       : [2, 1]
   # write     : [.new_array, .add, x]
   # read      : [num, .new_array, .add, x]
   # firstRead : [num]
   # live      : []
   save_context;
   param 3;
   .new_array := call #new_array;
   load_context;
   x := .new_array;
   .add := add 0, 1;
   x[.add] := 3;
   if greater_or_equal(num, 1) goto .if_false;

   # Block     : 1
   # adj       : [3]
   # write     : [num_aux]
   # read      : []
   # firstRead : []
   # live      : [num_aux]
   num_aux := 1;
   goto .if_next;

   # Block     : 2
   # adj       : [3]
   # write     : [.call, .add_A, .mult, .sub, num_aux]
   # read      : [.call, .add_A, .mult, num, .sub]
   # firstRead : [num]
   # live      : [num_aux]
 .if_false:
   .add_A := add num, 0;
   save_context;
   .sub := sub num, 1;
   param .sub;
   param this;
   .call := call Fac#ComputeFac;
   load_context;
   .mult := mult .add_A, .call;
   num_aux := .mult;

   # Block     : 3
   # adj       : []
   # write     : []
   # read      : [num_aux]
   # firstRead : [num_aux]
   # live      : []
 .if_next:
   return num_aux;
end
end

class Fac2:

procedure Fac2#method
   # Block     : 0
   # adj       : []
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   ops := 2;
   return 4;
end
end