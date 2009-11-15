
class Factorial {
  public static void main(String[] a) {
    System.out.println(new Fac().ComputeFac(10));
  }
}

class Fac {
  public int ComputeFac(int num) {
    int num_aux;
    int[] x;
    x = new int[3];
    x[0 + 1] = 3;
    if (num < 1) {
      num_aux = 1;
    } else {
      num_aux = (num+0) * (this.ComputeFac(num - 1));
    }
    return num_aux;
  }
}
