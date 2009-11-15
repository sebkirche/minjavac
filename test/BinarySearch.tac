Building symbol table...
Typechecking...
Building IR...
IR:

class BinarySearch:

procedure main(): 
    .new_BS := new BS;
    param .new_BS;
    param 20;
    .call := call Start;
    print .call;
end
end

class BS:
number
size

procedure Start(sz): aux01, aux02
    param this;
    param sz;
    .call := call Init;
    aux01 := .call;
    param this;
    .call_A := call Print;
    aux02 := .call_A;
    param this;
    param 8;
    .call_B := call Search;
    if is_true .call_B goto .if_true;

    goto .if_false;

 .if_true:
    print 1;
    goto .if_next;

 .if_false:
    print 0;

 .if_next:
    param this;
    param 19;
    .call_C := call Search;
    if is_true .call_C goto .if_true_A;

    goto .if_false_A;

 .if_true_A:
    print 1;
    goto .if_next_A;

 .if_false_A:
    print 0;

 .if_next_A:
    param this;
    param 20;
    .call_D := call Search;
    if is_true .call_D goto .if_true_B;

    goto .if_false_B;

 .if_true_B:
    print 1;
    goto .if_next_B;

 .if_false_B:
    print 0;

 .if_next_B:
    param this;
    param 21;
    .call_E := call Search;
    if is_true .call_E goto .if_true_C;

    goto .if_false_C;

 .if_true_C:
    print 1;
    goto .if_next_C;

 .if_false_C:
    print 0;

 .if_next_C:
    param this;
    param 37;
    .call_F := call Search;
    if is_true .call_F goto .if_true_D;

    goto .if_false_D;

 .if_true_D:
    print 1;
    goto .if_next_D;

 .if_false_D:
    print 0;

 .if_next_D:
    param this;
    param 38;
    .call_G := call Search;
    if is_true .call_G goto .if_true_E;

    goto .if_false_E;

 .if_true_E:
    print 1;
    goto .if_next_E;

 .if_false_E:
    print 0;

 .if_next_E:
    param this;
    param 39;
    .call_H := call Search;
    if is_true .call_H goto .if_true_F;

    goto .if_false_F;

 .if_true_F:
    print 1;
    goto .if_next_F;

 .if_false_F:
    print 0;

 .if_next_F:
    param this;
    param 50;
    .call_I := call Search;
    if is_true .call_I goto .if_true_G;

    goto .if_false_G;

 .if_true_G:
    print 1;
    goto .if_next_G;

 .if_false_G:
    print 0;

 .if_next_G:
    return 999;
end

procedure Search(num): bs01, right, left, var_cont, medium, aux01, nt
    aux01 := 0;
    bs01 := 0;
    .array_length := length number;
    right := .array_length;
    .sub := sub right, 1;
    right := .sub;
    left := 0;
    var_cont := 1;

 .loop:
    if is_true var_cont goto .while_true;

    goto .while_false;

 .while_true:
    .add := add left, right;
    medium := .add;
    param this;
    param medium;
    .call := call Div;
    medium := .call;
    .array_lookup := number[medium];
    aux01 := .array_lookup;
    if less_than(num, aux01) goto .if_true;

    goto .if_false;

 .if_true:
    .sub_A := sub medium, 1;
    right := .sub_A;
    goto .if_next;

 .if_false:
    .add_A := add medium, 1;
    left := .add_A;

 .if_next:
    param this;
    param num;
    param aux01;
    .call_A := call Compare;
    if is_true .call_A goto .if_true_A;

    goto .if_false_A;

 .if_true_A:
    var_cont := 0;
    goto .if_next_A;

 .if_false_A:
    var_cont := 1;

 .if_next_A:
    if less_than(right, left) goto .if_true_B;

    goto .if_false_B;

 .if_true_B:
    var_cont := 0;
    goto .if_next_B;

 .if_false_B:
    nt := 0;

 .if_next_B:
    goto .loop;

 .while_false:
    param this;
    param num;
    param aux01;
    .call_B := call Compare;
    if is_true .call_B goto .if_true_C;

    goto .if_false_C;

 .if_true_C:
    bs01 := 1;
    goto .if_next_C;

 .if_false_C:
    bs01 := 0;

 .if_next_C:
    return bs01;
end

procedure Div(num): count01, count02, aux03
    count01 := 0;
    count02 := 0;
    .sub := sub num, 1;
    aux03 := .sub;

 .loop:
    if less_than(count02, aux03) goto .while_true;

    goto .while_false;

 .while_true:
    .add := add count01, 1;
    count01 := .add;
    .add_A := add count02, 2;
    count02 := .add_A;
    goto .loop;

 .while_false:
    return count01;
end

procedure Compare(num1, num2): retval, aux02
    retval := 0;
    .add := add num2, 1;
    aux02 := .add;
    if less_than(num1, num2) goto .if_true;

    goto .if_false;

 .if_true:
    retval := 0;
    goto .if_next;

 .if_false:
    if less_than(num1, aux02) goto .if_false_A;

    goto .if_true_A;

 .if_true_A:
    retval := 0;
    goto .if_next_A;

 .if_false_A:
    retval := 1;

 .if_next_A:
 .if_next:
    return retval;
end

procedure Print(): j
    j := 1;

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
    print 99999;
    return 0;
end

procedure Init(sz): j, k, aux02, aux01
    size := sz;
    .new_array := new[] sz;
    number := .new_array;
    j := 1;
    .add := add size, 1;
    k := .add;

 .loop:
    if less_than(j, size) goto .while_true;

    goto .while_false;

 .while_true:
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

 .while_false:
    return 0;
end
end

Ok!
