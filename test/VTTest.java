
class VTTest {
  public static void main(String[] args) {
    Tester t = new Tester();

    t.test(new Base());
    t.test(new A());
    t.test(new B());
    t.test(new C());

    t.test2(new B());
    t.test2(new C());
  }
}

class Tester {
  public int test(Base obj) {
    obj.method();
    return 0;
  }

  public int test2(B obj) {
    obj.method2();
    return 0;
  }
}

class Base {
  public int method() {
    System.out.print("Base::method()\n");
    return 0;
  }
}

class A extends Base {
  public int method() {
    System.out.print("A::method()\n");
    return 0;
  }
}

class B extends Base {
  public int method2() {
    System.out.print("B::method2()\n");
    return 0;
  }

  public int method() {
    System.out.print("B::method()\n");
    return 0;
  }
}

class C extends B {
  public int method2() {
    System.out.print("C::method2()");
    return 0;
  }
}
