Building symbol table...
Typechecking...
Building IR...
IR:

class QuickSort:

procedure main(): 
    .new_QS := new QS;
    param .new_QS;
    param 10;
    .call := call Start;
    print .call;
end
end

class QS:
number
size

procedure Start(sz): aux01
    param this;
    param sz;
    .call := call Init;
    aux01 := .call;
    param this;
    .call_A := call Print;
    aux01 := .call_A;
    print 9999;
    .sub := sub size, 1;
    aux01 := .sub;
    param this;
    param aux01;
    param 0;
    .call_B := call Sort;
    aux01 := .call_B;
    param this;
    .call_C := call Print;
    aux01 := .call_C;
    return 0;
end

procedure Sort(left, right): v, i, j, nt, t, cont01, cont02, aux03
    t := 0;
    if less_than(left, right) goto .if_true;

    goto .if_false;

 .if_true:
    .array_lookup := number[right];
    v := .array_lookup;
    .sub := sub left, 1;
    i := .sub;
    j := right;
    cont01 := 1;

 .loop:
    if is_true cont01 goto .while_true;

    goto .while_false;

 .while_true:
    cont02 := 1;

 .loop_A:
    if is_true cont02 goto .while_true_A;

    goto .while_false_A;

 .while_true_A:
    .add := add i, 1;
    i := .add;
    .array_lookup_A := number[i];
    aux03 := .array_lookup_A;
    if less_than(aux03, v) goto .if_false_A;

    goto .if_true_A;

 .if_true_A:
    cont02 := 0;
    goto .if_next_A;

 .if_false_A:
    cont02 := 1;

 .if_next_A:
    goto .loop_A;

 .while_false_A:
    cont02 := 1;

 .loop_B:
    if is_true cont02 goto .while_true_B;

    goto .while_false_B;

 .while_true_B:
    .sub_A := sub j, 1;
    j := .sub_A;
    .array_lookup_B := number[j];
    aux03 := .array_lookup_B;
    if less_than(v, aux03) goto .if_false_B;

    goto .if_true_B;

 .if_true_B:
    cont02 := 0;
    goto .if_next_B;

 .if_false_B:
    cont02 := 1;

 .if_next_B:
    goto .loop_B;

 .while_false_B:
    .array_lookup_C := number[i];
    t := .array_lookup_C;
    .array_lookup_D := number[j];
    number[i] := .array_lookup_D;
    number[j] := t;
    .add_A := add i, 1;
    if less_than(j, .add_A) goto .if_true_C;

    goto .if_false_C;

 .if_true_C:
    cont01 := 0;
    goto .if_next_C;

 .if_false_C:
    cont01 := 1;

 .if_next_C:
    goto .loop;

 .while_false:
    .array_lookup_E := number[i];
    number[j] := .array_lookup_E;
    .array_lookup_F := number[right];
    number[i] := .array_lookup_F;
    number[right] := t;
    param this;
    .sub_B := sub i, 1;
    param .sub_B;
    param left;
    .call := call Sort;
    nt := .call;
    param this;
    param right;
    .add_B := add i, 1;
    param .add_B;
    .call_A := call Sort;
    nt := .call_A;
    goto .if_next;

 .if_false:
    nt := 0;

 .if_next:
    return 0;
end

procedure Print(): j
    j := 0;

 .loop:
    if less_than(j, size) goto .while_true;

    goto .while_false;

 .while_true:
    .array_lookup := number[j];
    print .array_lookup;
    .add := add j, 1;
    j := .add;
    goto .loop;

 .while_false:
    return 0;
end

procedure Init(sz): 
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
