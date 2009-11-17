Building symbol table...
Typechecking...
Building IR...
IR:

class BubbleSort:

procedure BubbleSort::main
   .new_BBS := new BBS;
   save_context;
   param .new_BBS;
   param 10;
   .call := call BBS::Start;
   load_context;
   print .call;
end
end

class BBS:

procedure BBS::Start
   save_context;
   param this;
   param sz;
   .call := call BBS::Init;
   load_context;
   aux01 := .call;
   save_context;
   param this;
   .call_A := call BBS::Print;
   load_context;
   aux01 := .call_A;
   print 99999;
   save_context;
   param this;
   .call_B := call BBS::Sort;
   load_context;
   aux01 := .call_B;
   save_context;
   param this;
   .call_C := call BBS::Print;
   load_context;
   aux01 := .call_C;
   return 0;
end

procedure BBS::Sort
   .sub := sub size, 1;
   i := .sub;
   .sub_A := sub 0, 1;
   aux02 := .sub_A;

 .loop:
   if greater_or_equal(aux02, i) goto .while_false;

   j := 1;

 .loop_A:
   .add := add i, 1;
   if greater_or_equal(j, .add) goto .while_false_A;

   .sub_B := sub j, 1;
   aux07 := .sub_B;
   .array_lookup := number[aux07];
   aux04 := .array_lookup;
   .array_lookup_A := number[j];
   aux05 := .array_lookup_A;
   if greater_or_equal(aux05, aux04) goto .if_false;

   .sub_C := sub j, 1;
   aux06 := .sub_C;
   .array_lookup_B := number[aux06];
   t := .array_lookup_B;
   .array_lookup_C := number[j];
   number[aux06] := .array_lookup_C;
   number[j] := t;
   goto .if_next;

 .if_false:
   nt := 0;

 .if_next:
   .add_A := add j, 1;
   j := .add_A;
   goto .loop_A;

 .while_false_A:
   .sub_D := sub i, 1;
   i := .sub_D;
   goto .loop;

 .while_false:
   return 0;
end

procedure BBS::Print
   j := 0;

 .loop:
   if greater_or_equal(j, size) goto .while_false;

   .array_lookup := number[j];
   print .array_lookup;
   .add := add j, 1;
   j := .add;
   goto .loop;

 .while_false:
   return 0;
end

procedure BBS::Init
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
