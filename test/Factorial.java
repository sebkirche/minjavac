
class Factorial {
  public static void main(String[] a) {
    System.out.println(new Fac().ComputeFac(10));
  }
}

class Fac {
  public int ComputeFac(int num) {
    int i;
    i = 0;

    while (i < num) {
      System.out.println(i);
      i = i + 1;
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
