Building symbol table...
Typechecking...
Building IR...
IR:

class TreeVisitor:

procedure main(): 
    .new_TV := new TV;
    param .new_TV;
    .call := call TV%Start;
    print .call;
end
end

class TV:

procedure Start(): root, ntb, nti, v
    .new_Tree := new Tree;
    root := .new_Tree;
    param root;
    param 16;
    .call := call Tree%Init;
    ntb := .call;
    param root;
    .call_A := call Tree%Print;
    ntb := .call_A;
    print 100000000;
    param root;
    param 8;
    .call_B := call Tree%Insert;
    ntb := .call_B;
    param root;
    param 24;
    .call_C := call Tree%Insert;
    ntb := .call_C;
    param root;
    param 4;
    .call_D := call Tree%Insert;
    ntb := .call_D;
    param root;
    param 12;
    .call_E := call Tree%Insert;
    ntb := .call_E;
    param root;
    param 20;
    .call_F := call Tree%Insert;
    ntb := .call_F;
    param root;
    param 28;
    .call_G := call Tree%Insert;
    ntb := .call_G;
    param root;
    param 14;
    .call_H := call Tree%Insert;
    ntb := .call_H;
    param root;
    .call_I := call Tree%Print;
    ntb := .call_I;
    print 100000000;
    .new_MyVisitor := new MyVisitor;
    v := .new_MyVisitor;
    print 50000000;
    param root;
    param v;
    .call_0A := call Tree%accept;
    nti := .call_0A;
    print 100000000;
    param root;
    param 24;
    .call_AA := call Tree%Search;
    print .call_AA;
    param root;
    param 12;
    .call_BA := call Tree%Search;
    print .call_BA;
    param root;
    param 16;
    .call_CA := call Tree%Search;
    print .call_CA;
    param root;
    param 50;
    .call_DA := call Tree%Search;
    print .call_DA;
    param root;
    param 12;
    .call_EA := call Tree%Search;
    print .call_EA;
    param root;
    param 12;
    .call_FA := call Tree%Delete;
    ntb := .call_FA;
    param root;
    .call_GA := call Tree%Print;
    ntb := .call_GA;
    param root;
    param 12;
    .call_HA := call Tree%Search;
    print .call_HA;
    return 0;
end
end

class Tree:
left
right
key
has_left
has_right
my_null

procedure Init(v_key): 
    key := v_key;
    has_left := 0;
    has_right := 0;
    return 1;
end

procedure SetRight(rn): 
    right := rn;
    return 1;
end

procedure SetLeft(ln): 
    left := ln;
    return 1;
end

procedure GetRight(): 
    return right;
end

procedure GetLeft(): 
    return left;
end

procedure GetKey(): 
    return key;
end

procedure SetKey(v_key): 
    key := v_key;
    return 1;
end

procedure GetHas_Right(): 
    return has_right;
end

procedure GetHas_Left(): 
    return has_left;
end

procedure SetHas_Left(val): 
    has_left := val;
    return 1;
end

procedure SetHas_Right(val): 
    has_right := val;
    return 1;
end

procedure Compare(num1, num2): ntb, nti
    ntb := 0;
    .add := add num2, 1;
    nti := .add;
    if less_than(num1, num2) goto .if_true;

    goto .if_false;

 .if_true:
    ntb := 0;
    goto .if_next;

 .if_false:
    if less_than(num1, nti) goto .if_false_A;

    goto .if_true_A;

 .if_true_A:
    ntb := 0;
    goto .if_next_A;

 .if_false_A:
    ntb := 1;

 .if_next_A:
 .if_next:
    return ntb;
end

procedure Insert(v_key): new_node, ntb, current_node, cont, key_aux
    .new_Tree := new Tree;
    new_node := .new_Tree;
    param new_node;
    param v_key;
    .call := call Tree%Init;
    ntb := .call;
    current_node := this;
    cont := 1;

 .loop:
    if is_true cont goto .while_true;

    goto .while_false;

 .while_true:
    param current_node;
    .call_A := call Tree%GetKey;
    key_aux := .call_A;
    if less_than(v_key, key_aux) goto .if_true;

    goto .if_false;

 .if_true:
    param current_node;
    .call_B := call Tree%GetHas_Left;
    if is_true .call_B goto .if_true_A;

    goto .if_false_A;

 .if_true_A:
    param current_node;
    .call_C := call Tree%GetLeft;
    current_node := .call_C;
    goto .if_next_A;

 .if_false_A:
    cont := 0;
    param current_node;
    param 1;
    .call_D := call Tree%SetHas_Left;
    ntb := .call_D;
    param current_node;
    param new_node;
    .call_E := call Tree%SetLeft;
    ntb := .call_E;

 .if_next_A:
    goto .if_next;

 .if_false:
    param current_node;
    .call_F := call Tree%GetHas_Right;
    if is_true .call_F goto .if_true_B;

    goto .if_false_B;

 .if_true_B:
    param current_node;
    .call_G := call Tree%GetRight;
    current_node := .call_G;
    goto .if_next_B;

 .if_false_B:
    cont := 0;
    param current_node;
    param 1;
    .call_H := call Tree%SetHas_Right;
    ntb := .call_H;
    param current_node;
    param new_node;
    .call_I := call Tree%SetRight;
    ntb := .call_I;

 .if_next_B:
 .if_next:
    goto .loop;

 .while_false:
    return 1;
end

procedure Delete(v_key): current_node, parent_node, cont, found, ntb, is_root, key_aux
    current_node := this;
    parent_node := this;
    cont := 1;
    found := 0;
    is_root := 1;

 .loop:
    if is_true cont goto .while_true;

    goto .while_false;

 .while_true:
    param current_node;
    .call := call Tree%GetKey;
    key_aux := .call;
    if less_than(v_key, key_aux) goto .if_true;

    goto .if_false;

 .if_true:
    param current_node;
    .call_A := call Tree%GetHas_Left;
    if is_true .call_A goto .if_true_A;

    goto .if_false_A;

 .if_true_A:
    parent_node := current_node;
    param current_node;
    .call_B := call Tree%GetLeft;
    current_node := .call_B;
    goto .if_next_A;

 .if_false_A:
    cont := 0;

 .if_next_A:
    goto .if_next;

 .if_false:
    if less_than(key_aux, v_key) goto .if_true_B;

    goto .if_false_B;

 .if_true_B:
    param current_node;
    .call_C := call Tree%GetHas_Right;
    if is_true .call_C goto .if_true_C;

    goto .if_false_C;

 .if_true_C:
    parent_node := current_node;
    param current_node;
    .call_D := call Tree%GetRight;
    current_node := .call_D;
    goto .if_next_C;

 .if_false_C:
    cont := 0;

 .if_next_C:
    goto .if_next_B;

 .if_false_B:
    if is_true is_root goto .if_true_D;

    goto .if_false_D;

 .if_true_D:
    param current_node;
    .call_E := call Tree%GetHas_Right;
    if is_true .call_E goto .if_false_E;

    goto .and_cont;

 .and_cont:
    param current_node;
    .call_F := call Tree%GetHas_Left;
    if is_true .call_F goto .if_false_E;

    goto .if_true_E;

 .if_true_E:
    ntb := 1;
    goto .if_next_E;

 .if_false_E:
    param this;
    param current_node;
    param parent_node;
    .call_G := call Tree%Remove;
    ntb := .call_G;

 .if_next_E:
    goto .if_next_D;

 .if_false_D:
    param this;
    param current_node;
    param parent_node;
    .call_H := call Tree%Remove;
    ntb := .call_H;

 .if_next_D:
    found := 1;
    cont := 0;

 .if_next_B:
 .if_next:
    is_root := 0;
    goto .loop;

 .while_false:
    return found;
end

procedure Remove(p_node, c_node): ntb, auxkey1, auxkey2
    param c_node;
    .call := call Tree%GetHas_Left;
    if is_true .call goto .if_true;

    goto .if_false;

 .if_true:
    param this;
    param c_node;
    param p_node;
    .call_A := call Tree%RemoveLeft;
    ntb := .call_A;
    goto .if_next;

 .if_false:
    param c_node;
    .call_B := call Tree%GetHas_Right;
    if is_true .call_B goto .if_true_A;

    goto .if_false_A;

 .if_true_A:
    param this;
    param c_node;
    param p_node;
    .call_C := call Tree%RemoveRight;
    ntb := .call_C;
    goto .if_next_A;

 .if_false_A:
    param c_node;
    .call_D := call Tree%GetKey;
    auxkey1 := .call_D;
    param p_node;
    .call_F := call Tree%GetLeft;
    param .call_F;
    .call_E := call Tree%GetKey;
    auxkey2 := .call_E;
    param this;
    param auxkey2;
    param auxkey1;
    .call_G := call Tree%Compare;
    if is_true .call_G goto .if_true_B;

    goto .if_false_B;

 .if_true_B:
    param p_node;
    param my_null;
    .call_H := call Tree%SetLeft;
    ntb := .call_H;
    param p_node;
    param 0;
    .call_I := call Tree%SetHas_Left;
    ntb := .call_I;
    goto .if_next_B;

 .if_false_B:
    param p_node;
    param my_null;
    .call_0A := call Tree%SetRight;
    ntb := .call_0A;
    param p_node;
    param 0;
    .call_AA := call Tree%SetHas_Right;
    ntb := .call_AA;

 .if_next_B:
 .if_next_A:
 .if_next:
    return 1;
end

procedure RemoveRight(p_node, c_node): ntb
 .loop:
    param c_node;
    .call := call Tree%GetHas_Right;
    if is_true .call goto .while_true;

    goto .while_false;

 .while_true:
    param c_node;
    param c_node;
    .call_C := call Tree%GetRight;
    param .call_C;
    .call_B := call Tree%GetKey;
    param .call_B;
    .call_A := call Tree%SetKey;
    ntb := .call_A;
    p_node := c_node;
    param c_node;
    .call_D := call Tree%GetRight;
    c_node := .call_D;
    goto .loop;

 .while_false:
    param p_node;
    param my_null;
    .call_E := call Tree%SetRight;
    ntb := .call_E;
    param p_node;
    param 0;
    .call_F := call Tree%SetHas_Right;
    ntb := .call_F;
    return 1;
end

procedure RemoveLeft(p_node, c_node): ntb
 .loop:
    param c_node;
    .call := call Tree%GetHas_Left;
    if is_true .call goto .while_true;

    goto .while_false;

 .while_true:
    param c_node;
    param c_node;
    .call_C := call Tree%GetLeft;
    param .call_C;
    .call_B := call Tree%GetKey;
    param .call_B;
    .call_A := call Tree%SetKey;
    ntb := .call_A;
    p_node := c_node;
    param c_node;
    .call_D := call Tree%GetLeft;
    c_node := .call_D;
    goto .loop;

 .while_false:
    param p_node;
    param my_null;
    .call_E := call Tree%SetLeft;
    ntb := .call_E;
    param p_node;
    param 0;
    .call_F := call Tree%SetHas_Left;
    ntb := .call_F;
    return 1;
end

procedure Search(v_key): current_node, ifound, cont, key_aux
    current_node := this;
    cont := 1;
    ifound := 0;

 .loop:
    if is_true cont goto .while_true;

    goto .while_false;

 .while_true:
    param current_node;
    .call := call Tree%GetKey;
    key_aux := .call;
    if less_than(v_key, key_aux) goto .if_true;

    goto .if_false;

 .if_true:
    param current_node;
    .call_A := call Tree%GetHas_Left;
    if is_true .call_A goto .if_true_A;

    goto .if_false_A;

 .if_true_A:
    param current_node;
    .call_B := call Tree%GetLeft;
    current_node := .call_B;
    goto .if_next_A;

 .if_false_A:
    cont := 0;

 .if_next_A:
    goto .if_next;

 .if_false:
    if less_than(key_aux, v_key) goto .if_true_B;

    goto .if_false_B;

 .if_true_B:
    param current_node;
    .call_C := call Tree%GetHas_Right;
    if is_true .call_C goto .if_true_C;

    goto .if_false_C;

 .if_true_C:
    param current_node;
    .call_D := call Tree%GetRight;
    current_node := .call_D;
    goto .if_next_C;

 .if_false_C:
    cont := 0;

 .if_next_C:
    goto .if_next_B;

 .if_false_B:
    ifound := 1;
    cont := 0;

 .if_next_B:
 .if_next:
    goto .loop;

 .while_false:
    return ifound;
end

procedure Print(): ntb, current_node
    current_node := this;
    param this;
    param current_node;
    .call := call Tree%RecPrint;
    ntb := .call;
    return 1;
end

procedure RecPrint(node): ntb
    param node;
    .call := call Tree%GetHas_Left;
    if is_true .call goto .if_true;

    goto .if_false;

 .if_true:
    param this;
    param node;
    .call_B := call Tree%GetLeft;
    param .call_B;
    .call_A := call Tree%RecPrint;
    ntb := .call_A;
    goto .if_next;

 .if_false:
    ntb := 1;

 .if_next:
    param node;
    .call_C := call Tree%GetKey;
    print .call_C;
    param node;
    .call_D := call Tree%GetHas_Right;
    if is_true .call_D goto .if_true_A;

    goto .if_false_A;

 .if_true_A:
    param this;
    param node;
    .call_F := call Tree%GetRight;
    param .call_F;
    .call_E := call Tree%RecPrint;
    ntb := .call_E;
    goto .if_next_A;

 .if_false_A:
    ntb := 1;

 .if_next_A:
    return 1;
end

procedure accept(v): nti
    print 333;
    param v;
    param this;
    .call := call Visitor%visit;
    nti := .call;
    return 0;
end
end

class Visitor:
l
r

procedure visit(n): nti
    param n;
    .call := call Tree%GetHas_Right;
    if is_true .call goto .if_true;

    goto .if_false;

 .if_true:
    param n;
    .call_A := call Tree%GetRight;
    r := .call_A;
    param r;
    param this;
    .call_B := call Tree%accept;
    nti := .call_B;
    goto .if_next;

 .if_false:
    nti := 0;

 .if_next:
    param n;
    .call_C := call Tree%GetHas_Left;
    if is_true .call_C goto .if_true_A;

    goto .if_false_A;

 .if_true_A:
    param n;
    .call_D := call Tree%GetLeft;
    l := .call_D;
    param l;
    param this;
    .call_E := call Tree%accept;
    nti := .call_E;
    goto .if_next_A;

 .if_false_A:
    nti := 0;

 .if_next_A:
    return 0;
end
end

class MyVisitor:

procedure visit(n): nti
    param n;
    .call := call Tree%GetHas_Right;
    if is_true .call goto .if_true;

    goto .if_false;

 .if_true:
    param n;
    .call_A := call Tree%GetRight;
    r := .call_A;
    param r;
    param this;
    .call_B := call Tree%accept;
    nti := .call_B;
    goto .if_next;

 .if_false:
    nti := 0;

 .if_next:
    param n;
    .call_C := call Tree%GetKey;
    print .call_C;
    param n;
    .call_D := call Tree%GetHas_Left;
    if is_true .call_D goto .if_true_A;

    goto .if_false_A;

 .if_true_A:
    param n;
    .call_E := call Tree%GetLeft;
    l := .call_E;
    param l;
    param this;
    .call_F := call Tree%accept;
    nti := .call_F;
    goto .if_next_A;

 .if_false_A:
    nti := 0;

 .if_next_A:
    return 0;
end
end

Ok!
