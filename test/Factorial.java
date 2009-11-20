
class Factorial {
  public static void main(String[] a) {
    System.out.println(new Fac().ComputeFac(10));
  }
}

class Fac {
  public int ComputeFac(int num) {
    int i;
    int j;
    i = 0;
    j = i+1;
    
    while (i < num) {
      System.out.println(i);
      i = i + 2*j + 1 - 4 + 4 - 3 + j;
      j = (j + i) + 5 - i * 7;
    }

    return num;
  }
/*
class Fac2 extends Fac {
  public int method() {
    ops = 2;
    return 4;
  }
*/
}
