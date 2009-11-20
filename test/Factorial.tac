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
   ; write     : [q, .add, j, k, i]
   ; read      : [.add, i]
   ; firstRead : []
   ; live      : [q, j, k]
   i := 0;
   .add := add i, 1;
   j := .add;
   q := 1;
   k := 2;

   ; Block     : 1
   ; adj       : [3, 2]
   ; write     : []
   ; read      : [k]
   ; firstRead : [k]
   ; live      : []
 .loop:
   if greater_or_equal(0, k) goto .while_false;

   ; Block     : 2
   ; adj       : [1]
   ; write     : [.mult_A, .mult_B, .mult_C, .mult_D, k, i, .add_A, .mult, .add_B, .add_C, .sub, .sub_A, q, .sub_B, .add_D, .add_E, .add_F]
   ; read      : [.mult_A, .mult_B, .mult_C, .mult_D, j, k, i, .add_A, .mult, .add_B, .add_C, .sub_A, q, .sub, .sub_B, .add_D, .add_E, .add_F]
   ; firstRead : [q, j, k]
   ; live      : [q, k]
   .mult := mult j, q;
   .sub := sub .mult, k;
   .add_A := add .sub, 5;
   i := .add_A;
   .add_D := add q, k;
   .add_C := add .add_D, 5;
   .add_B := add .add_C, 4;
   .mult_C := mult j, i;
   .mult_B := mult .mult_C, k;
   .mult_A := mult .mult_B, q;
   .sub_A := sub .add_B, .mult_A;
   q := .sub_A;
   .add_F := add q, j;
   .add_E := add .add_F, i;
   .sub_B := sub .add_E, k;
   .mult_D := mult .sub_B, 3;
   k := .mult_D;
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