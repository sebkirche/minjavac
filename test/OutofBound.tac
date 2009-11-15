Building symbol table...
Typechecking...
Building IR...
IR:

class OutofBound:

procedure main(): 
    .new_LS := new LS;
    param .new_LS;
    param 10;
    .call := call Start;
    print .call;
end
end

class LS:
number
size
j
k
nt
ls01
aux01
aux02
ifound

procedure Start(sz): 
    param this;
    param sz;
    .call := call Init;
    aux01 := .call;
    param this;
    .call_A := call Print;
    aux02 := .call_A;
    return 55;
end

procedure Print(): 
    j := 0;

 .loop:
    .add := add size, 1;
    if less_than(j, .add) goto .while_true;

    goto .while_false;

 .while_true:
    .array_lookup := number[j];
    print .array_lookup;
    .add_A := add j, 1;
    j := .add_A;
    goto .loop;

 .while_false:
    return 0;
end

procedure Init(sz): 
    size := sz;
    .new_array := new[] sz;
    number := .new_array;
    j := 0;
    .add := add size, 1;
    k := .add;

 .loop:
    if less_than(j, size) goto .and_cont;

    goto .while_false;

 .and_cont:
    if less_than(j, 0) goto .while_false;

    goto .while_true;

 .while_true:
    .mult := mult 2, j;
    aux01 := .mult;
    .add_A := sub k, 3;
    aux02 := .add_A;
    .add_B := add aux01, aux02;
    number[j] := .add_B;
    .add_C := add j, 1;
    j := .add_C;
    .add_D := sub k, 1;
    k := .add_D;
    goto .loop;

 .while_false:
    return 0;
end
end

Ok!
