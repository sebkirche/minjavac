
class Factorial {
  public static void main(String[] a) {
    System.out.println(new Fac().ComputeFac(5));
  }
}

class Fac {
  int facNum;

  public int ComputeFac(int num) {
    int num_aux;
    int[] x;
    int[] y;
    
    x = new int[3];
    x[0 + 1] = 3;
    y = x;
    y[0 + 2] = facNum;
    
    if (num < 1) {
      num_aux = 1;
    } else {
      num_aux = num * this.ComputeFac(num - 1);
    }
    return num_aux;
  }
}
