class Factorial:

procedure Factorial@main
   ; Block     : 0
   ; adj       : []
   ; write     : [.call, .void, .new_Fac]
   ; read      : [.call, .new_Fac]
   ; firstRead : []
   ; live      : []
   save_context;
   .new_Fac := call Fac@@new;
   load_context;
   save_context;
   param 10;
   param .new_Fac;
   .call := call Fac@ComputeFac;
   load_context;
   save_c_context;
   param .call;
   .void := call _print_int;
   load_c_context;
end
end

class Fac:

procedure Fac@ComputeFac
   ; Block     : 0
   ; adj       : [1]
   ; write     : [.add, j, i]
   ; read      : [.add, i]
   ; firstRead : []
   ; live      : [j, i]
   i := 0;
   .add := add i, 1;
   j := .add;

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
   ; write     : [.void, .mult_A, j, i, .add_A, .mult, .add_B, .add_C, .sub_A, .sub, .sub_B, .add_D, .add_E, .add_F]
   ; read      : [.mult_A, j, i, .add_A, .mult, .add_B, .add_C, .sub_A, .sub, .sub_B, .add_D, .add_E, .add_F]
   ; firstRead : [j, i]
   ; live      : [j, i]
   save_c_context;
   param i;
   .void := call _print_int;
   load_c_context;
   .mult := mult 2, j;
   .add_D := add i, .mult;
   .add_C := add .add_D, 1;
   .sub_A := sub .add_C, 4;
   .add_B := add .sub_A, 4;
   .sub := sub .add_B, 3;
   .add_A := add .sub, j;
   i := .add_A;
   .add_F := add j, i;
   .add_E := add .add_F, 5;
   .mult_A := mult i, 7;
   .sub_B := sub .add_E, .mult_A;
   j := .sub_B;
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