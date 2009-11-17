Building symbol table...
Typechecking...
Building IR...
IR:

class BinaryTree:

procedure BinaryTree::main
   # Block     : 0
   # adj       : []
   # write     : [.call, .new_BT]
   # read      : [.call, .new_BT]
   # firstRead : []
   # live      : []
   .new_BT := new BT;
   save_context;
   param .new_BT;
   .call := call BT::Start;
   load_context;
   print .call;
end
end

class BT:

procedure BT::Start
   # Block     : 0
   # adj       : []
   # write     : [.call_HA, ntb, root, .call_A, .call_B, .call_AA, .call_EA, .call_GA, .call_FA, .call_CA, .call_BA, .new_Tree, .call, .call_0A, .call_DA, .call_D, .call_C, .call_F, .call_E, .call_H, .call_G, .call_I]
   # read      : [.call_HA, root, .call_A, .call_B, .call_AA, .call_EA, .call_GA, .call_FA, .call_CA, .call_BA, .new_Tree, .call, .call_0A, .call_DA, .call_D, .call_C, .call_F, .call_E, .call_H, .call_G, .call_I]
   # firstRead : []
   # live      : []
   .new_Tree := new Tree;
   root := .new_Tree;
   save_context;
   param root;
   param 16;
   .call := call Tree::Init;
   load_context;
   ntb := .call;
   save_context;
   param root;
   .call_A := call Tree::Print;
   load_context;
   ntb := .call_A;
   print 100000000;
   save_context;
   param root;
   param 8;
   .call_B := call Tree::Insert;
   load_context;
   ntb := .call_B;
   save_context;
   param root;
   .call_C := call Tree::Print;
   load_context;
   ntb := .call_C;
   save_context;
   param root;
   param 24;
   .call_D := call Tree::Insert;
   load_context;
   ntb := .call_D;
   save_context;
   param root;
   param 4;
   .call_E := call Tree::Insert;
   load_context;
   ntb := .call_E;
   save_context;
   param root;
   param 12;
   .call_F := call Tree::Insert;
   load_context;
   ntb := .call_F;
   save_context;
   param root;
   param 20;
   .call_G := call Tree::Insert;
   load_context;
   ntb := .call_G;
   save_context;
   param root;
   param 28;
   .call_H := call Tree::Insert;
   load_context;
   ntb := .call_H;
   save_context;
   param root;
   param 14;
   .call_I := call Tree::Insert;
   load_context;
   ntb := .call_I;
   save_context;
   param root;
   .call_0A := call Tree::Print;
   load_context;
   ntb := .call_0A;
   save_context;
   param root;
   param 24;
   .call_AA := call Tree::Search;
   load_context;
   print .call_AA;
   save_context;
   param root;
   param 12;
   .call_BA := call Tree::Search;
   load_context;
   print .call_BA;
   save_context;
   param root;
   param 16;
   .call_CA := call Tree::Search;
   load_context;
   print .call_CA;
   save_context;
   param root;
   param 50;
   .call_DA := call Tree::Search;
   load_context;
   print .call_DA;
   save_context;
   param root;
   param 12;
   .call_EA := call Tree::Search;
   load_context;
   print .call_EA;
   save_context;
   param root;
   param 12;
   .call_FA := call Tree::Delete;
   load_context;
   ntb := .call_FA;
   save_context;
   param root;
   .call_GA := call Tree::Print;
   load_context;
   ntb := .call_GA;
   save_context;
   param root;
   param 12;
   .call_HA := call Tree::Search;
   load_context;
   print .call_HA;
   return 0;
end
end

class Tree:

procedure Tree::Init
   # Block     : 0
   # adj       : []
   # write     : []
   # read      : [v_key]
   # firstRead : [v_key]
   # live      : []
   key := v_key;
   has_left := 0;
   has_right := 0;
   return 1;
end

procedure Tree::SetRight
   # Block     : 0
   # adj       : []
   # write     : []
   # read      : [rn]
   # firstRead : [rn]
   # live      : []
   right := rn;
   return 1;
end

procedure Tree::SetLeft
   # Block     : 0
   # adj       : []
   # write     : []
   # read      : [ln]
   # firstRead : [ln]
   # live      : []
   left := ln;
   return 1;
end

procedure Tree::GetRight
   # Block     : 0
   # adj       : []
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   return right;
end

procedure Tree::GetLeft
   # Block     : 0
   # adj       : []
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   return left;
end

procedure Tree::GetKey
   # Block     : 0
   # adj       : []
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   return key;
end

procedure Tree::SetKey
   # Block     : 0
   # adj       : []
   # write     : []
   # read      : [v_key]
   # firstRead : [v_key]
   # live      : []
   key := v_key;
   return 1;
end

procedure Tree::GetHas_Right
   # Block     : 0
   # adj       : []
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   return has_right;
end

procedure Tree::GetHas_Left
   # Block     : 0
   # adj       : []
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
   return has_left;
end

procedure Tree::SetHas_Left
   # Block     : 0
   # adj       : []
   # write     : []
   # read      : [val]
   # firstRead : [val]
   # live      : []
   has_left := val;
   return 1;
end

procedure Tree::SetHas_Right
   # Block     : 0
   # adj       : []
   # write     : []
   # read      : [val]
   # firstRead : [val]
   # live      : []
   has_right := val;
   return 1;
end

procedure Tree::Compare
   # Block     : 0
   # adj       : [2, 1]
   # write     : [ntb, nti, .add]
   # read      : [.add, num2, num1]
   # firstRead : [num2, num1]
   # live      : [ntb, nti]
   ntb := 0;
   .add := add num2, 1;
   nti := .add;
   if greater_or_equal(num1, num2) goto .if_false;

   # Block     : 1
   # adj       : [5]
   # write     : [ntb]
   # read      : []
   # firstRead : []
   # live      : [ntb]
   ntb := 0;
   goto .if_next;

   # Block     : 2
   # adj       : [4, 3]
   # write     : []
   # read      : [nti, num1]
   # firstRead : [nti, num1]
   # live      : []
 .if_false:
   if less_than(num1, nti) goto .if_false_A;

   # Block     : 3
   # adj       : [5]
   # write     : [ntb]
   # read      : []
   # firstRead : []
   # live      : [ntb]
   ntb := 0;
   goto .if_next_A;

   # Block     : 4
   # adj       : [5]
   # write     : [ntb]
   # read      : []
   # firstRead : []
   # live      : [ntb]
 .if_false_A:
   ntb := 1;

   # Block     : 5
   # adj       : []
   # write     : []
   # read      : [ntb]
   # firstRead : [ntb]
   # live      : []
 .if_next_A:
 .if_next:
   return ntb;
end

procedure Tree::Insert
   # Block     : 0
   # adj       : [1]
   # write     : [.call, ntb, new_node, cont, current_node, .new_Tree]
   # read      : [.call, new_node, v_key, .new_Tree]
   # firstRead : [v_key]
   # live      : [new_node, cont, current_node]
   .new_Tree := new Tree;
   new_node := .new_Tree;
   save_context;
   param new_node;
   param v_key;
   .call := call Tree::Init;
   load_context;
   ntb := .call;
   current_node := this;
   cont := 1;

   # Block     : 1
   # adj       : [11, 2]
   # write     : []
   # read      : [cont]
   # firstRead : [cont]
   # live      : []
 .loop:
   if is_false cont goto .while_false;

   # Block     : 2
   # adj       : [7, 3]
   # write     : [.call_A, key_aux]
   # read      : [.call_A, v_key, key_aux, current_node]
   # firstRead : [v_key, current_node]
   # live      : []
   save_context;
   param current_node;
   .call_A := call Tree::GetKey;
   load_context;
   key_aux := .call_A;
   if greater_or_equal(v_key, key_aux) goto .if_false;

   # Block     : 3
   # adj       : [5, 4]
   # write     : [.call_B]
   # read      : [.call_B, current_node]
   # firstRead : [current_node]
   # live      : []
   save_context;
   param current_node;
   .call_B := call Tree::GetHas_Left;
   load_context;
   if is_false .call_B goto .if_false_A;

   # Block     : 4
   # adj       : [6]
   # write     : [.call_C, current_node]
   # read      : [.call_C, current_node]
   # firstRead : [current_node]
   # live      : [current_node]
   save_context;
   param current_node;
   .call_C := call Tree::GetLeft;
   load_context;
   current_node := .call_C;
   goto .if_next_A;

   # Block     : 5
   # adj       : [6]
   # write     : [ntb, .call_D, .call_E, cont]
   # read      : [new_node, .call_D, .call_E, current_node]
   # firstRead : [new_node, current_node]
   # live      : [cont]
 .if_false_A:
   cont := 0;
   save_context;
   param current_node;
   param 1;
   .call_D := call Tree::SetHas_Left;
   load_context;
   ntb := .call_D;
   save_context;
   param current_node;
   param new_node;
   .call_E := call Tree::SetLeft;
   load_context;
   ntb := .call_E;

   # Block     : 6
   # adj       : [10]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_A:
   goto .if_next;

   # Block     : 7
   # adj       : [9, 8]
   # write     : [.call_F]
   # read      : [.call_F, current_node]
   # firstRead : [current_node]
   # live      : []
 .if_false:
   save_context;
   param current_node;
   .call_F := call Tree::GetHas_Right;
   load_context;
   if is_false .call_F goto .if_false_B;

   # Block     : 8
   # adj       : [10]
   # write     : [.call_G, current_node]
   # read      : [.call_G, current_node]
   # firstRead : [current_node]
   # live      : [current_node]
   save_context;
   param current_node;
   .call_G := call Tree::GetRight;
   load_context;
   current_node := .call_G;
   goto .if_next_B;

   # Block     : 9
   # adj       : [10]
   # write     : [ntb, .call_H, cont, .call_I]
   # read      : [new_node, .call_H, .call_I, current_node]
   # firstRead : [new_node, current_node]
   # live      : [cont]
 .if_false_B:
   cont := 0;
   save_context;
   param current_node;
   param 1;
   .call_H := call Tree::SetHas_Right;
   load_context;
   ntb := .call_H;
   save_context;
   param current_node;
   param new_node;
   .call_I := call Tree::SetRight;
   load_context;
   ntb := .call_I;

   # Block     : 10
   # adj       : [1]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_B:
 .if_next:
   goto .loop;

   # Block     : 11
   # adj       : []
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .while_false:
   return 1;
end

procedure Tree::Delete
   # Block     : 0
   # adj       : [1]
   # write     : [is_root, parent_node, cont, found, current_node]
   # read      : []
   # firstRead : []
   # live      : [parent_node, is_root, cont, current_node, found]
   current_node := this;
   parent_node := this;
   cont := 1;
   found := 0;
   is_root := 1;

   # Block     : 1
   # adj       : [21, 2]
   # write     : []
   # read      : [cont]
   # firstRead : [cont]
   # live      : []
 .loop:
   if is_false cont goto .while_false;

   # Block     : 2
   # adj       : [7, 3]
   # write     : [.call, key_aux]
   # read      : [.call, v_key, key_aux, current_node]
   # firstRead : [v_key, current_node]
   # live      : [key_aux]
   save_context;
   param current_node;
   .call := call Tree::GetKey;
   load_context;
   key_aux := .call;
   if greater_or_equal(v_key, key_aux) goto .if_false;

   # Block     : 3
   # adj       : [5, 4]
   # write     : [.call_A]
   # read      : [.call_A, current_node]
   # firstRead : [current_node]
   # live      : []
   save_context;
   param current_node;
   .call_A := call Tree::GetHas_Left;
   load_context;
   if is_false .call_A goto .if_false_A;

   # Block     : 4
   # adj       : [6]
   # write     : [parent_node, .call_B, current_node]
   # read      : [.call_B, current_node]
   # firstRead : [current_node]
   # live      : [parent_node, current_node]
   parent_node := current_node;
   save_context;
   param current_node;
   .call_B := call Tree::GetLeft;
   load_context;
   current_node := .call_B;
   goto .if_next_A;

   # Block     : 5
   # adj       : [6]
   # write     : [cont]
   # read      : []
   # firstRead : []
   # live      : [cont]
 .if_false_A:
   cont := 0;

   # Block     : 6
   # adj       : [20]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_A:
   goto .if_next;

   # Block     : 7
   # adj       : [12, 8]
   # write     : []
   # read      : [v_key, key_aux]
   # firstRead : [v_key, key_aux]
   # live      : []
 .if_false:
   if greater_or_equal(key_aux, v_key) goto .if_false_B;

   # Block     : 8
   # adj       : [10, 9]
   # write     : [.call_C]
   # read      : [.call_C, current_node]
   # firstRead : [current_node]
   # live      : []
   save_context;
   param current_node;
   .call_C := call Tree::GetHas_Right;
   load_context;
   if is_false .call_C goto .if_false_C;

   # Block     : 9
   # adj       : [11]
   # write     : [parent_node, .call_D, current_node]
   # read      : [.call_D, current_node]
   # firstRead : [current_node]
   # live      : [parent_node, current_node]
   parent_node := current_node;
   save_context;
   param current_node;
   .call_D := call Tree::GetRight;
   load_context;
   current_node := .call_D;
   goto .if_next_C;

   # Block     : 10
   # adj       : [11]
   # write     : [cont]
   # read      : []
   # firstRead : []
   # live      : [cont]
 .if_false_C:
   cont := 0;

   # Block     : 11
   # adj       : [20]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_C:
   goto .if_next_B;

   # Block     : 12
   # adj       : [18, 13]
   # write     : []
   # read      : [is_root]
   # firstRead : [is_root]
   # live      : []
 .if_false_B:
   if is_false is_root goto .if_false_D;

   # Block     : 13
   # adj       : [16, 14]
   # write     : [.call_E]
   # read      : [.call_E, current_node]
   # firstRead : [current_node]
   # live      : []
   save_context;
   param current_node;
   .call_E := call Tree::GetHas_Right;
   load_context;
   if is_true .call_E goto .if_false_E;

   # Block     : 14
   # adj       : [16, 15]
   # write     : [.call_F]
   # read      : [.call_F, current_node]
   # firstRead : [current_node]
   # live      : []
   save_context;
   param current_node;
   .call_F := call Tree::GetHas_Left;
   load_context;
   if is_true .call_F goto .if_false_E;

   # Block     : 15
   # adj       : [17]
   # write     : [ntb]
   # read      : []
   # firstRead : []
   # live      : []
   ntb := 1;
   goto .if_next_E;

   # Block     : 16
   # adj       : [17]
   # write     : [ntb, .call_G]
   # read      : [parent_node, .call_G, current_node]
   # firstRead : [parent_node, current_node]
   # live      : []
 .if_false_E:
   save_context;
   param this;
   param current_node;
   param parent_node;
   .call_G := call Tree::Remove;
   load_context;
   ntb := .call_G;

   # Block     : 17
   # adj       : [19]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_E:
   goto .if_next_D;

   # Block     : 18
   # adj       : [19]
   # write     : [ntb, .call_H]
   # read      : [parent_node, .call_H, current_node]
   # firstRead : [parent_node, current_node]
   # live      : []
 .if_false_D:
   save_context;
   param this;
   param current_node;
   param parent_node;
   .call_H := call Tree::Remove;
   load_context;
   ntb := .call_H;

   # Block     : 19
   # adj       : [20]
   # write     : [cont, found]
   # read      : []
   # firstRead : []
   # live      : [cont, found]
 .if_next_D:
   found := 1;
   cont := 0;

   # Block     : 20
   # adj       : [1]
   # write     : [is_root]
   # read      : []
   # firstRead : []
   # live      : [is_root]
 .if_next_B:
 .if_next:
   is_root := 0;
   goto .loop;

   # Block     : 21
   # adj       : []
   # write     : []
   # read      : [found]
   # firstRead : [found]
   # live      : []
 .while_false:
   return found;
end

procedure Tree::Remove
   # Block     : 0
   # adj       : [2, 1]
   # write     : [.call]
   # read      : [.call, c_node]
   # firstRead : [c_node]
   # live      : []
   save_context;
   param c_node;
   .call := call Tree::GetHas_Left;
   load_context;
   if is_false .call goto .if_false;

   # Block     : 1
   # adj       : [7]
   # write     : [ntb, .call_A]
   # read      : [p_node, .call_A, c_node]
   # firstRead : [p_node, c_node]
   # live      : []
   save_context;
   param this;
   param c_node;
   param p_node;
   .call_A := call Tree::RemoveLeft;
   load_context;
   ntb := .call_A;
   goto .if_next;

   # Block     : 2
   # adj       : [4, 3]
   # write     : [.call_B]
   # read      : [.call_B, c_node]
   # firstRead : [c_node]
   # live      : []
 .if_false:
   save_context;
   param c_node;
   .call_B := call Tree::GetHas_Right;
   load_context;
   if is_false .call_B goto .if_false_A;

   # Block     : 3
   # adj       : [7]
   # write     : [ntb, .call_C]
   # read      : [p_node, .call_C, c_node]
   # firstRead : [p_node, c_node]
   # live      : []
   save_context;
   param this;
   param c_node;
   param p_node;
   .call_C := call Tree::RemoveRight;
   load_context;
   ntb := .call_C;
   goto .if_next_A;

   # Block     : 4
   # adj       : [6, 5]
   # write     : [auxkey1, .call_D, auxkey2, .call_F, .call_E, .call_G]
   # read      : [p_node, auxkey1, .call_D, auxkey2, .call_F, c_node, .call_E, .call_G]
   # firstRead : [p_node, c_node]
   # live      : []
 .if_false_A:
   save_context;
   param c_node;
   .call_D := call Tree::GetKey;
   load_context;
   auxkey1 := .call_D;
   save_context;
   param p_node;
   .call_E := call Tree::GetLeft;
   load_context;
   save_context;
   param .call_E;
   .call_F := call Tree::GetKey;
   load_context;
   auxkey2 := .call_F;
   save_context;
   param this;
   param auxkey2;
   param auxkey1;
   .call_G := call Tree::Compare;
   load_context;
   if is_false .call_G goto .if_false_B;

   # Block     : 5
   # adj       : [7]
   # write     : [ntb, .call_H, .call_I]
   # read      : [p_node, .call_H, .call_I]
   # firstRead : [p_node]
   # live      : []
   save_context;
   param p_node;
   param my_null;
   .call_H := call Tree::SetLeft;
   load_context;
   ntb := .call_H;
   save_context;
   param p_node;
   param 0;
   .call_I := call Tree::SetHas_Left;
   load_context;
   ntb := .call_I;
   goto .if_next_B;

   # Block     : 6
   # adj       : [7]
   # write     : [.call_0A, ntb, .call_AA]
   # read      : [.call_0A, p_node, .call_AA]
   # firstRead : [p_node]
   # live      : []
 .if_false_B:
   save_context;
   param p_node;
   param my_null;
   .call_0A := call Tree::SetRight;
   load_context;
   ntb := .call_0A;
   save_context;
   param p_node;
   param 0;
   .call_AA := call Tree::SetHas_Right;
   load_context;
   ntb := .call_AA;

   # Block     : 7
   # adj       : []
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_B:
 .if_next_A:
 .if_next:
   return 1;
end

procedure Tree::RemoveRight
   # Block     : 0
   # adj       : [2, 1]
   # write     : [.call]
   # read      : [.call, c_node]
   # firstRead : [c_node]
   # live      : []
 .loop:
   save_context;
   param c_node;
   .call := call Tree::GetHas_Right;
   load_context;
   if is_false .call goto .while_false;

   # Block     : 1
   # adj       : [0]
   # write     : [p_node, ntb, .call_A, .call_B, .call_D, .call_C, c_node]
   # read      : [.call_A, .call_B, .call_D, .call_C, c_node]
   # firstRead : [c_node]
   # live      : [p_node, c_node]
   save_context;
   param c_node;
   save_context;
   param c_node;
   .call_A := call Tree::GetRight;
   load_context;
   save_context;
   param .call_A;
   .call_B := call Tree::GetKey;
   load_context;
   param .call_B;
   .call_C := call Tree::SetKey;
   load_context;
   ntb := .call_C;
   p_node := c_node;
   save_context;
   param c_node;
   .call_D := call Tree::GetRight;
   load_context;
   c_node := .call_D;
   goto .loop;

   # Block     : 2
   # adj       : []
   # write     : [ntb, .call_F, .call_E]
   # read      : [p_node, .call_F, .call_E]
   # firstRead : [p_node]
   # live      : []
 .while_false:
   save_context;
   param p_node;
   param my_null;
   .call_E := call Tree::SetRight;
   load_context;
   ntb := .call_E;
   save_context;
   param p_node;
   param 0;
   .call_F := call Tree::SetHas_Right;
   load_context;
   ntb := .call_F;
   return 1;
end

procedure Tree::RemoveLeft
   # Block     : 0
   # adj       : [2, 1]
   # write     : [.call]
   # read      : [.call, c_node]
   # firstRead : [c_node]
   # live      : []
 .loop:
   save_context;
   param c_node;
   .call := call Tree::GetHas_Left;
   load_context;
   if is_false .call goto .while_false;

   # Block     : 1
   # adj       : [0]
   # write     : [p_node, ntb, .call_A, .call_B, .call_D, .call_C, c_node]
   # read      : [.call_A, .call_B, .call_D, .call_C, c_node]
   # firstRead : [c_node]
   # live      : [p_node, c_node]
   save_context;
   param c_node;
   save_context;
   param c_node;
   .call_A := call Tree::GetLeft;
   load_context;
   save_context;
   param .call_A;
   .call_B := call Tree::GetKey;
   load_context;
   param .call_B;
   .call_C := call Tree::SetKey;
   load_context;
   ntb := .call_C;
   p_node := c_node;
   save_context;
   param c_node;
   .call_D := call Tree::GetLeft;
   load_context;
   c_node := .call_D;
   goto .loop;

   # Block     : 2
   # adj       : []
   # write     : [ntb, .call_F, .call_E]
   # read      : [p_node, .call_F, .call_E]
   # firstRead : [p_node]
   # live      : []
 .while_false:
   save_context;
   param p_node;
   param my_null;
   .call_E := call Tree::SetLeft;
   load_context;
   ntb := .call_E;
   save_context;
   param p_node;
   param 0;
   .call_F := call Tree::SetHas_Left;
   load_context;
   ntb := .call_F;
   return 1;
end

procedure Tree::Search
   # Block     : 0
   # adj       : [1]
   # write     : [ifound, cont, current_node]
   # read      : []
   # firstRead : []
   # live      : [ifound, cont, current_node]
   current_node := this;
   cont := 1;
   ifound := 0;

   # Block     : 1
   # adj       : [14, 2]
   # write     : []
   # read      : [cont]
   # firstRead : [cont]
   # live      : []
 .loop:
   if is_false cont goto .while_false;

   # Block     : 2
   # adj       : [7, 3]
   # write     : [.call, key_aux]
   # read      : [.call, v_key, key_aux, current_node]
   # firstRead : [v_key, current_node]
   # live      : [key_aux]
   save_context;
   param current_node;
   .call := call Tree::GetKey;
   load_context;
   key_aux := .call;
   if greater_or_equal(v_key, key_aux) goto .if_false;

   # Block     : 3
   # adj       : [5, 4]
   # write     : [.call_A]
   # read      : [.call_A, current_node]
   # firstRead : [current_node]
   # live      : []
   save_context;
   param current_node;
   .call_A := call Tree::GetHas_Left;
   load_context;
   if is_false .call_A goto .if_false_A;

   # Block     : 4
   # adj       : [6]
   # write     : [.call_B, current_node]
   # read      : [.call_B, current_node]
   # firstRead : [current_node]
   # live      : [current_node]
   save_context;
   param current_node;
   .call_B := call Tree::GetLeft;
   load_context;
   current_node := .call_B;
   goto .if_next_A;

   # Block     : 5
   # adj       : [6]
   # write     : [cont]
   # read      : []
   # firstRead : []
   # live      : [cont]
 .if_false_A:
   cont := 0;

   # Block     : 6
   # adj       : [13]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_A:
   goto .if_next;

   # Block     : 7
   # adj       : [12, 8]
   # write     : []
   # read      : [v_key, key_aux]
   # firstRead : [v_key, key_aux]
   # live      : []
 .if_false:
   if greater_or_equal(key_aux, v_key) goto .if_false_B;

   # Block     : 8
   # adj       : [10, 9]
   # write     : [.call_C]
   # read      : [.call_C, current_node]
   # firstRead : [current_node]
   # live      : []
   save_context;
   param current_node;
   .call_C := call Tree::GetHas_Right;
   load_context;
   if is_false .call_C goto .if_false_C;

   # Block     : 9
   # adj       : [11]
   # write     : [.call_D, current_node]
   # read      : [.call_D, current_node]
   # firstRead : [current_node]
   # live      : [current_node]
   save_context;
   param current_node;
   .call_D := call Tree::GetRight;
   load_context;
   current_node := .call_D;
   goto .if_next_C;

   # Block     : 10
   # adj       : [11]
   # write     : [cont]
   # read      : []
   # firstRead : []
   # live      : [cont]
 .if_false_C:
   cont := 0;

   # Block     : 11
   # adj       : [13]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_C:
   goto .if_next_B;

   # Block     : 12
   # adj       : [13]
   # write     : [ifound, cont]
   # read      : []
   # firstRead : []
   # live      : [ifound, cont]
 .if_false_B:
   ifound := 1;
   cont := 0;

   # Block     : 13
   # adj       : [1]
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_B:
 .if_next:
   goto .loop;

   # Block     : 14
   # adj       : []
   # write     : []
   # read      : [ifound]
   # firstRead : [ifound]
   # live      : []
 .while_false:
   return ifound;
end

procedure Tree::Print
   # Block     : 0
   # adj       : []
   # write     : [.call, ntb, current_node]
   # read      : [.call, current_node]
   # firstRead : []
   # live      : []
   current_node := this;
   save_context;
   param this;
   param current_node;
   .call := call Tree::RecPrint;
   load_context;
   ntb := .call;
   return 1;
end

procedure Tree::RecPrint
   # Block     : 0
   # adj       : [2, 1]
   # write     : [.call]
   # read      : [.call, node]
   # firstRead : [node]
   # live      : []
   save_context;
   param node;
   .call := call Tree::GetHas_Left;
   load_context;
   if is_false .call goto .if_false;

   # Block     : 1
   # adj       : [3]
   # write     : [ntb, .call_A, .call_B]
   # read      : [node, .call_A, .call_B]
   # firstRead : [node]
   # live      : []
   save_context;
   param this;
   save_context;
   param node;
   .call_A := call Tree::GetLeft;
   load_context;
   param .call_A;
   .call_B := call Tree::RecPrint;
   load_context;
   ntb := .call_B;
   goto .if_next;

   # Block     : 2
   # adj       : [3]
   # write     : [ntb]
   # read      : []
   # firstRead : []
   # live      : []
 .if_false:
   ntb := 1;

   # Block     : 3
   # adj       : [5, 4]
   # write     : [.call_D, .call_C]
   # read      : [node, .call_D, .call_C]
   # firstRead : [node]
   # live      : []
 .if_next:
   save_context;
   param node;
   .call_C := call Tree::GetKey;
   load_context;
   print .call_C;
   save_context;
   param node;
   .call_D := call Tree::GetHas_Right;
   load_context;
   if is_false .call_D goto .if_false_A;

   # Block     : 4
   # adj       : [6]
   # write     : [ntb, .call_F, .call_E]
   # read      : [node, .call_F, .call_E]
   # firstRead : [node]
   # live      : []
   save_context;
   param this;
   save_context;
   param node;
   .call_E := call Tree::GetRight;
   load_context;
   param .call_E;
   .call_F := call Tree::RecPrint;
   load_context;
   ntb := .call_F;
   goto .if_next_A;

   # Block     : 5
   # adj       : [6]
   # write     : [ntb]
   # read      : []
   # firstRead : []
   # live      : []
 .if_false_A:
   ntb := 1;

   # Block     : 6
   # adj       : []
   # write     : []
   # read      : []
   # firstRead : []
   # live      : []
 .if_next_A:
   return 1;
end
end

Ok!
