#include <stdlib.h>
#include <stdio.h>

void* alloc(int size) {
  return malloc(size);
}

int* new_array(int size) {
  int* array = (int*)alloc(4*(size+1));
  array[0] = size;
  return array;
}

void print_int(int n) {
  printf("%d\n", n);
}

extern asmMain(void);

int main(void) {
  asmMain();
  return 0;
}
