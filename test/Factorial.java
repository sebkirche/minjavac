
class Factorial {
  public static void main(String[] a) {
    System.out.println(new Fac().ComputeFac(10));
  }
}

class Fac {
  public int ComputeFac(int num) {
    int i;
    int j;
    int k;
    int q;

    i = 0;
    j = i+1;
    q = 1;
    k = 2;
    
    while (0 < k) {
      i = j * q - k + 5;
      q = q + k + 5 + 4 - j*i*k*q;
      k = (q+j+i-k)*3;
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
