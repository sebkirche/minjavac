Building symbol table...
Typechecking...
Building IR...
IR:

class QuickSort:

procedure QuickSort::main
   # Block     : 0
   # adj       : []
   # write     : [.new_QS, .call]
   # read      : [.new_QS, .call]
   # firstRead : []
   # live      : []
   .new_QS := new QS;
   save_context;
   param .new_QS;
   param 10;
   .call := call QS::Start;
   load_context;
   print .call;
end
end

class QS:

procedure QS::Start
   # Block     : 0
   # adj       : []
   # write     : [.call, .sub, .call_A, .call_B, .call_C, aux01]
   # read      : [.call, .sub, .call_A, .call_B, .call_C, aux01, sz]
   # firstRead : [sz]
   # live      : []
   save_context;
   param this;
   param sz;
   .call := call QS::Init;
   load_context;
   aux01 := .call;
   save_context;
   param this;
   .call_A := call QS::Print;
   load_context;
   aux01 := .call_A;
   print 9999;
   .sub := sub size, 1;
   aux01 := .sub;
   save_context;
   param this;
   param aux01;
   param 0;
   .call_B := call QS::Sort;
   load_context;
   aux01 := .call_B;
   save_context;
   param this;
   .call_C := call QS::Print;
   load_context;
   aux01 := .call_C;
   return 0;
end

procedure QS::Sort
   # Block     : 0
   # adj       : [20, 1]
   # write     : [t]
   # read      : [left, right]
   # firstRead : [left, right]
   # live      : [t]
   t := 0;
   if greater_or_equal(left, right) goto .if_false;

   # Block     : 1
   # adj       : [2]
   # write     : [v, cont01, .sub, .array_lookup, j, i]
   # read      : [.sub, left, .array_lookup, right]
   # firstRead : [left, right]
   # live      : [v, cont01, j, i]
   .array_lookup := number[right];
   v := .array_lookup;
   .sub := sub left, 1;
   i := .sub;
   j := right;
   cont01 := 1;

   # Block     : 2
   # adj       : [19, 3]
   # write     : []
   # read      : [cont01]
   # firstRead : [cont01]
   # live      : []
 .loop:
   if is_false cont01 goto .while_false;

   # Block     : 3
   # adj       : [4]
   # write     : [cont02]
   # read      : []
   # firstRead : []
   # live      : [cont02]
   cont02 := 1;

   # Block     : 4
   # adj       : [9, 5]
   # write     : []
   # read      : [cont02]
   # firstRead : [cont02]
   # live      : []
 .loop_A:
   if is_false cont02 goto .while_false_A;

   # Block     : 5
   # adj       : [7, 6]
   # write     : [.array_lookup_A, aux03, .add, i]
   # read      : [v, .array_lookup_A, aux03, .add, i]
   # firstRead : [v, i]
   # live      : [i]
   .add := add i, 1;
   i := .add;
   .array_lookup_A := number[i];
   aux03 := .array_lookup_A;
   if less_than(aux03, v) goto .if_false_A;

   # Block     : 6
   # adj       : [8]
   # write     : [cont02]
   # read      : []
   # firstRead : []
   # live      : [cont02]
   cont02 := 0;
   goto .if_next_A;

   # Block     : 7
   # adj       : [8]
   # write     : [cont02]
   # read      : []
   # firstRead : []
   # live      : [cont02]
 .if_false_A:
   cont02 := 1;

   # Block     : 8
   # adj       : [4]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_A:
   goto .loop_A;

   # Block     : 9
   # adj       : [10]
   # write     : [cont02]
   # read      : []
   # firstRead : []
   # live      : [cont02]
 .while_false_A:
   cont02 := 1;

   # Block     : 10
   # adj       : [15, 11]
   # write     : []
   # read      : [cont02]
   # firstRead : [cont02]
   # live      : []
 .loop_B:
   if is_false cont02 goto .while_false_B;

   # Block     : 11
   # adj       : [13, 12]
   # write     : [.array_lookup_B, .sub_A, aux03, j]
   # read      : [v, .array_lookup_B, .sub_A, aux03, j]
   # firstRead : [v, j]
   # live      : [j]
   .sub_A := sub j, 1;
   j := .sub_A;
   .array_lookup_B := number[j];
   aux03 := .array_lookup_B;
   if less_than(v, aux03) goto .if_false_B;

   # Block     : 12
   # adj       : [14]
   # write     : [cont02]
   # read      : []
   # firstRead : []
   # live      : [cont02]
   cont02 := 0;
   goto .if_next_B;

   # Block     : 13
   # adj       : [14]
   # write     : [cont02]
   # read      : []
   # firstRead : []
   # live      : [cont02]
 .if_false_B:
   cont02 := 1;

   # Block     : 14
   # adj       : [10]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_B:
   goto .loop_B;

   # Block     : 15
   # adj       : [17, 16]
   # write     : [.add_A, t, .array_lookup_D, .array_lookup_C]
   # read      : [.add_A, t, .array_lookup_D, .array_lookup_C, j, i]
   # firstRead : [j, i]
   # live      : [t]
 .while_false_B:
   .array_lookup_C := number[i];
   t := .array_lookup_C;
   .array_lookup_D := number[j];
   number[i] := .array_lookup_D;
   number[j] := t;
   .add_A := add i, 1;
   if greater_or_equal(j, .add_A) goto .if_false_C;

   # Block     : 16
   # adj       : [18]
   # write     : [cont01]
   # read      : []
   # firstRead : []
   # live      : [cont01]
   cont01 := 0;
   goto .if_next_C;

   # Block     : 17
   # adj       : [18]
   # write     : [cont01]
   # read      : []
   # firstRead : []
   # live      : [cont01]
 .if_false_C:
   cont01 := 1;

   # Block     : 18
   # adj       : [2]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_C:
   goto .loop;

   # Block     : 19
   # adj       : [21]
   # write     : [nt, .call, .add_B, .array_lookup_F, .array_lookup_E, .call_A, .sub_B]
   # read      : [.call, .add_B, .array_lookup_F, t, .array_lookup_E, .call_A, .sub_B, left, right, i]
   # firstRead : [t, left, right, i]
   # live      : []
 .while_false:
   .array_lookup_E := number[i];
   number[j] := .array_lookup_E;
   .array_lookup_F := number[right];
   number[i] := .array_lookup_F;
   number[right] := t;
   save_context;
   param this;
   .sub_B := sub i, 1;
   param .sub_B;
   param left;
   .call := call QS::Sort;
   load_context;
   nt := .call;
   save_context;
   param this;
   param right;
   .add_B := add i, 1;
   param .add_B;
   .call_A := call QS::Sort;
   load_context;
   nt := .call_A;
   goto .if_next;

   # Block     : 20
   # adj       : [21]
   # write     : [nt]
   # read      : []
   # firstRead : []
   # live      : []
 .if_false:
   nt := 0;

   # Block     : 21
   # adj       : []
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next:
   return 0;
end

procedure QS::Print
   # Block     : 0
   # adj       : [1]
   # write     : [j]
   # read      : []
   # firstRead : []
   # live      : [j]
   j := 0;

   # Block     : 1
   # adj       : [3, 2]
   # write     : []
   # read      : [j]
   # firstRead : [j]
   # live      : []
 .loop:
   if greater_or_equal(j, size) goto .while_false;

   # Block     : 2
   # adj       : [1]
   # write     : [.add, .array_lookup, j]
   # read      : [.add, .array_lookup, j]
   # firstRead : [j]
   # live      : [j]
   .array_lookup := number[j];
   print .array_lookup;
   .add := add j, 1;
   j := .add;
   goto .loop;

   # Block     : 3
   # adj       : []
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .while_false:
   return 0;
end

procedure QS::Init
   # Block     : 0
   # adj       : []
   # write     : [.new_array]
   # read      : [.new_array, sz]
   # firstRead : [sz]
   # live      : []
   size := sz;
   .new_array := new[] sz;
   number := .new_array;
   number[0] := 20;
   number[1] := 7;
   number[2] := 12;
   number[3] := 18;
   number[4] := 2;
   number[5] := 11;
   number[6] := 6;
   number[7] := 9;
   number[8] := 19;
   number[9] := 5;
   return 0;
end
end

Ok!
