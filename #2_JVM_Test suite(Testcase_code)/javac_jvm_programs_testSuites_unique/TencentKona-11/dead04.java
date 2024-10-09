



package vm.compiler.jbe.dead.dead04;


class struct {
    int i1 = 2;
    int i2 = 3;
    int i3 = 4;
    int i4 = 5;
    int i5 = 6;
    int i6 = 7;
    int i7 = 8;
    int i8 = 9;
}

public class dead04 {

  public static void main(String args[]) {
    dead04 dce = new dead04();

    System.out.println("f()="+dce.f()+"; fopt()="+dce.fopt());
    if (dce.f() == dce.fopt()) {
      System.out.println("Test dead04 Passed.");
    } else {
      throw new Error("Test dead04 Failed: f()=" + dce.f() + " != fopt()=" + dce.fopt());
    }
  }

  int f() {
    struct s = new struct();

    s.i1 = 1;
    s.i2 = 2;
    s.i3 = 3;
    s.i4 = 4;
    s.i5 = 5;
    s.i6 = 6;
    s.i7 = 7;
    s.i8 = 8;

    s.i1 = 1;
    s.i2 = 2;
    s.i3 = 3;
    s.i4 = 4;
    s.i5 = 5;
    s.i6 = 6;
    s.i7 = 7;
    s.i8 = 8;

    s.i1 = 1;
    s.i2 = 2;
    s.i3 = 3;
    s.i4 = 4;
    s.i5 = 5;
    s.i6 = 6;
    s.i7 = 7;
    s.i8 = 8;

    return s.i8;
  }

  
  int fopt() {
    struct s = new struct();

    s.i8 = 8;
    return s.i8;
  }
}
