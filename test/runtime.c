#include <stdlib.h>
#include <stdio.h>

void* alloc(int size) {
  return malloc(size);
}

void new_array(int size) {
  int* array = (int*)alloc(4*(size+1));
  array[0] = size;
}

void print_int(int n) {
  printf("%d\n", n);
}
