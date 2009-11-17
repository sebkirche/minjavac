Building symbol table...
Typechecking...
Building IR...
IR:

class Factorial:

procedure Factorial::main
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
    .new_array := new[] 3;
    x := .new_array;
    .add := add 0, 1;
    x[.add] := 3;
    if greater_or_equal(num, 1) goto .if_false;

    num_aux := 1;
    goto .if_next;

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

 .if_next:
    return num_aux;
end
end

Ok!
