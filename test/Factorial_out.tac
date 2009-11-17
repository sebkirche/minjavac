Building symbol table...
Typechecking...
Building IR...
IR:

class Factorial:

procedure Factorial::main
   # Block     : 0
   # adj       : []
   # write     : [.call, .new_Fac]
   # read      : [.call, .new_Fac]
   # firstRead : []
   # live      : []
   .new_Fac := new Fac;
   save_context;
   param .new_Fac;
   param 10;
   .call := call Fac::ComputeFac;
   load_context;
   print .call;
end
end

class Fac:

procedure Fac::ComputeFac
   # Block     : 0
   # adj       : [2, 1]
   # write     : [.new_array, .add, x]
   # read      : [num, .new_array]
   # firstRead : [num]
   # live      : []
   .new_array := new[] 3;
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
   param this;
   .sub := sub num, 1;
   param .sub;
   .call := call Fac::ComputeFac;
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

procedure Fac2::method
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

Ok!
