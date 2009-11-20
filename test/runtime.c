#include <stdlib.h>
#include <stdio.h>

int alloc(int size) {
  return (int)malloc(size);
}

void new_array(int size) {
  int* array = (int*)alloc(4*(size+1));
  array[0] = size;
}

void print_int(int n) {
  printf("%d\n", n);
}

extern asmMain(void);

int main(void) {
  asmMain();
  return 0;
}
