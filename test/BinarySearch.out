Building symbol table...
Typechecking...
Building IR...
IR:

class BinarySearch:

procedure BinarySearch::main
   # Block     : 0
   # adj       : []
   # write     : [.call, .new_BS]
   # read      : [.call, .new_BS]
   # firstRead : []
   # live      : []
   .new_BS := new BS;
   save_context;
   param .new_BS;
   param 20;
   .call := call BS::Start;
   load_context;
   print .call;
end
end

class BS:

procedure BS::Start
   # Block     : 0
   # adj       : [2, 1]
   # write     : [.call, .call_A, .call_B, aux02, aux01]
   # read      : [.call, .call_A, .call_B, sz]
   # firstRead : [sz]
   # live      : []
   save_context;
   param this;
   param sz;
   .call := call BS::Init;
   load_context;
   aux01 := .call;
   save_context;
   param this;
   .call_A := call BS::Print;
   load_context;
   aux02 := .call_A;
   save_context;
   param this;
   param 8;
   .call_B := call BS::Search;
   load_context;
   if is_false .call_B goto .if_false;

   # Block     : 1
   # adj       : [3]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   print 1;
   goto .if_next;

   # Block     : 2
   # adj       : [3]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_false:
   print 0;

   # Block     : 3
   # adj       : [5, 4]
   # write     : [.call_C]
   # read      : [.call_C]
   # firstRead : []
   # live      : []
 .if_next:
   save_context;
   param this;
   param 19;
   .call_C := call BS::Search;
   load_context;
   if is_false .call_C goto .if_false_A;

   # Block     : 4
   # adj       : [6]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   print 1;
   goto .if_next_A;

   # Block     : 5
   # adj       : [6]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_false_A:
   print 0;

   # Block     : 6
   # adj       : [8, 7]
   # write     : [.call_D]
   # read      : [.call_D]
   # firstRead : []
   # live      : []
 .if_next_A:
   save_context;
   param this;
   param 20;
   .call_D := call BS::Search;
   load_context;
   if is_false .call_D goto .if_false_B;

   # Block     : 7
   # adj       : [9]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   print 1;
   goto .if_next_B;

   # Block     : 8
   # adj       : [9]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_false_B:
   print 0;

   # Block     : 9
   # adj       : [11, 10]
   # write     : [.call_E]
   # read      : [.call_E]
   # firstRead : []
   # live      : []
 .if_next_B:
   save_context;
   param this;
   param 21;
   .call_E := call BS::Search;
   load_context;
   if is_false .call_E goto .if_false_C;

   # Block     : 10
   # adj       : [12]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   print 1;
   goto .if_next_C;

   # Block     : 11
   # adj       : [12]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_false_C:
   print 0;

   # Block     : 12
   # adj       : [14, 13]
   # write     : [.call_F]
   # read      : [.call_F]
   # firstRead : []
   # live      : []
 .if_next_C:
   save_context;
   param this;
   param 37;
   .call_F := call BS::Search;
   load_context;
   if is_false .call_F goto .if_false_D;

   # Block     : 13
   # adj       : [15]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   print 1;
   goto .if_next_D;

   # Block     : 14
   # adj       : [15]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_false_D:
   print 0;

   # Block     : 15
   # adj       : [17, 16]
   # write     : [.call_G]
   # read      : [.call_G]
   # firstRead : []
   # live      : []
 .if_next_D:
   save_context;
   param this;
   param 38;
   .call_G := call BS::Search;
   load_context;
   if is_false .call_G goto .if_false_E;

   # Block     : 16
   # adj       : [18]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   print 1;
   goto .if_next_E;

   # Block     : 17
   # adj       : [18]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_false_E:
   print 0;

   # Block     : 18
   # adj       : [20, 19]
   # write     : [.call_H]
   # read      : [.call_H]
   # firstRead : []
   # live      : []
 .if_next_E:
   save_context;
   param this;
   param 39;
   .call_H := call BS::Search;
   load_context;
   if is_false .call_H goto .if_false_F;

   # Block     : 19
   # adj       : [21]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   print 1;
   goto .if_next_F;

   # Block     : 20
   # adj       : [21]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_false_F:
   print 0;

   # Block     : 21
   # adj       : [23, 22]
   # write     : [.call_I]
   # read      : [.call_I]
   # firstRead : []
   # live      : []
 .if_next_F:
   save_context;
   param this;
   param 50;
   .call_I := call BS::Search;
   load_context;
   if is_false .call_I goto .if_false_G;

   # Block     : 22
   # adj       : [24]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   print 1;
   goto .if_next_G;

   # Block     : 23
   # adj       : [24]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_false_G:
   print 0;

   # Block     : 24
   # adj       : []
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_G:
   return 999;
end

procedure BS::Search
   # Block     : 0
   # adj       : [1]
   # write     : [.sub, var_cont, left, bs01, aux01, right, .array_length]
   # read      : [.sub, right, .array_length]
   # firstRead : []
   # live      : [var_cont, left, bs01, aux01, right]
   aux01 := 0;
   bs01 := 0;
   .array_length := length number;
   right := .array_length;
   .sub := sub right, 1;
   right := .sub;
   left := 0;
   var_cont := 1;

   # Block     : 1
   # adj       : [12, 2]
   # write     : []
   # read      : [var_cont]
   # firstRead : [var_cont]
   # live      : []
 .loop:
   if is_false var_cont goto .while_false;

   # Block     : 2
   # adj       : [4, 3]
   # write     : [.call, .add, aux01, .array_lookup, medium]
   # read      : [.call, num, .add, aux01, left, .array_lookup, right, medium]
   # firstRead : [num, left, right]
   # live      : [aux01, medium]
   .add := add left, right;
   medium := .add;
   save_context;
   param this;
   param medium;
   .call := call BS::Div;
   load_context;
   medium := .call;
   .array_lookup := number[medium];
   aux01 := .array_lookup;
   if greater_or_equal(num, aux01) goto .if_false;

   # Block     : 3
   # adj       : [5]
   # write     : [.sub_A, right]
   # read      : [.sub_A, medium]
   # firstRead : [medium]
   # live      : [right]
   .sub_A := sub medium, 1;
   right := .sub_A;
   goto .if_next;

   # Block     : 4
   # adj       : [5]
   # write     : [.add_A, left]
   # read      : [.add_A, medium]
   # firstRead : [medium]
   # live      : [left]
 .if_false:
   .add_A := add medium, 1;
   left := .add_A;

   # Block     : 5
   # adj       : [7, 6]
   # write     : [.call_A]
   # read      : [num, .call_A, aux01]
   # firstRead : [num, aux01]
   # live      : []
 .if_next:
   save_context;
   param this;
   param num;
   param aux01;
   .call_A := call BS::Compare;
   load_context;
   if is_false .call_A goto .if_false_A;

   # Block     : 6
   # adj       : [8]
   # write     : [var_cont]
   # read      : []
   # firstRead : []
   # live      : [var_cont]
   var_cont := 0;
   goto .if_next_A;

   # Block     : 7
   # adj       : [8]
   # write     : [var_cont]
   # read      : []
   # firstRead : []
   # live      : [var_cont]
 .if_false_A:
   var_cont := 1;

   # Block     : 8
   # adj       : [10, 9]
   # write     : []
   # read      : [left, right]
   # firstRead : [left, right]
   # live      : []
 .if_next_A:
   if greater_or_equal(right, left) goto .if_false_B;

   # Block     : 9
   # adj       : [11]
   # write     : [var_cont]
   # read      : []
   # firstRead : []
   # live      : [var_cont]
   var_cont := 0;
   goto .if_next_B;

   # Block     : 10
   # adj       : [11]
   # write     : [nt]
   # read      : []
   # firstRead : []
   # live      : []
 .if_false_B:
   nt := 0;

   # Block     : 11
   # adj       : [1]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_B:
   goto .loop;

   # Block     : 12
   # adj       : [14, 13]
   # write     : [.call_B]
   # read      : [num, .call_B, aux01]
   # firstRead : [num, aux01]
   # live      : []
 .while_false:
   save_context;
   param this;
   param num;
   param aux01;
   .call_B := call BS::Compare;
   load_context;
   if is_false .call_B goto .if_false_C;

   # Block     : 13
   # adj       : [15]
   # write     : [bs01]
   # read      : []
   # firstRead : []
   # live      : [bs01]
   bs01 := 1;
   goto .if_next_C;

   # Block     : 14
   # adj       : [15]
   # write     : [bs01]
   # read      : []
   # firstRead : []
   # live      : [bs01]
 .if_false_C:
   bs01 := 0;

   # Block     : 15
   # adj       : []
   # write     : []
   # read      : [bs01]
   # firstRead : [bs01]
   # live      : []
 .if_next_C:
   return bs01;
end

procedure BS::Div
   # Block     : 0
   # adj       : [1]
   # write     : [count02, count01, .sub, aux03]
   # read      : [num, .sub]
   # firstRead : [num]
   # live      : [count02, count01, aux03]
   count01 := 0;
   count02 := 0;
   .sub := sub num, 1;
   aux03 := .sub;

   # Block     : 1
   # adj       : [3, 2]
   # write     : []
   # read      : [count02, aux03]
   # firstRead : [count02, aux03]
   # live      : []
 .loop:
   if greater_or_equal(count02, aux03) goto .while_false;

   # Block     : 2
   # adj       : [1]
   # write     : [.add_A, count02, count01, .add]
   # read      : [.add_A, count02, count01, .add]
   # firstRead : [count02, count01]
   # live      : [count02, count01]
   .add := add count01, 1;
   count01 := .add;
   .add_A := add count02, 2;
   count02 := .add_A;
   goto .loop;

   # Block     : 3
   # adj       : []
   # write     : []
   # read      : [count01]
   # firstRead : [count01]
   # live      : []
 .while_false:
   return count01;
end

procedure BS::Compare
   # Block     : 0
   # adj       : [2, 1]
   # write     : [retval, .add, aux02]
   # read      : [.add, num2, num1]
   # firstRead : [num2, num1]
   # live      : [retval, aux02]
   retval := 0;
   .add := add num2, 1;
   aux02 := .add;
   if greater_or_equal(num1, num2) goto .if_false;

   # Block     : 1
   # adj       : [5]
   # write     : [retval]
   # read      : []
   # firstRead : []
   # live      : [retval]
   retval := 0;
   goto .if_next;

   # Block     : 2
   # adj       : [4, 3]
   # write     : []
   # read      : [aux02, num1]
   # firstRead : [aux02, num1]
   # live      : []
 .if_false:
   if less_than(num1, aux02) goto .if_false_A;

   # Block     : 3
   # adj       : [5]
   # write     : [retval]
   # read      : []
   # firstRead : []
   # live      : [retval]
   retval := 0;
   goto .if_next_A;

   # Block     : 4
   # adj       : [5]
   # write     : [retval]
   # read      : []
   # firstRead : []
   # live      : [retval]
 .if_false_A:
   retval := 1;

   # Block     : 5
   # adj       : []
   # write     : []
   # read      : [retval]
   # firstRead : [retval]
   # live      : []
 .if_next_A:
 .if_next:
   return retval;
end

procedure BS::Print
   # Block     : 0
   # adj       : [1]
   # write     : [j]
   # read      : []
   # firstRead : []
   # live      : [j]
   j := 1;

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
   print 99999;
   return 0;
end

procedure BS::Init
   # Block     : 0
   # adj       : [1]
   # write     : [.new_array, .add, j, k]
   # read      : [.new_array, .add, sz]
   # firstRead : [sz]
   # live      : [j, k]
   size := sz;
   .new_array := new[] sz;
   number := .new_array;
   j := 1;
   .add := add size, 1;
   k := .add;

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
   # write     : [.add_A, .add_B, .mult, .sub_A, .sub, aux02, aux01, j, k]
   # read      : [.add_A, .add_B, .mult, .sub_A, .sub, aux02, aux01, j, k]
   # firstRead : [j, k]
   # live      : [j, k]
   .mult := mult 2, j;
   aux01 := .mult;
   .sub := sub k, 3;
   aux02 := .sub;
   .add_A := add aux01, aux02;
   number[j] := .add_A;
   .add_B := add j, 1;
   j := .add_B;
   .sub_A := sub k, 1;
   k := .sub_A;
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
end

Ok!
