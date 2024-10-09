



package vm.compiler.jbe.dead.dead09;





public class dead09 {
  boolean bol = true;

  public static void main(String args[]) {
    dead09 dce = new dead09();

    System.out.println("f()="+dce.f()+"; fopt()="+dce.fopt());
    if (dce.f() == dce.fopt()) {
      System.out.println("Test dead09 Passed.");
    } else {
      throw new Error("Test dead09 Failed: f()=" + dce.f() + " != fopt()=" +dce.fopt());
    }
  }

  int f() {
    int i = 0;

    if (bol)
      i = 1;
    else if (bol)
      i = 2;
    else if (bol)
      i = 3;
    else if (bol)
      i = 4;
    else if (bol)
      i = 5;
    else if (bol)
      i = 6;
    else if (bol)
      i = 7;
    else if (bol)
      i = 8;

    if (bol)
      i = 1;
    else if (bol)
      i = 2;
    else if (bol)
      i = 3;
    else if (bol)
      i = 4;
    else if (bol)
      i = 5;
    else if (bol)
      i = 6;
    else if (bol)
      i = 7;
    else if (bol)
      i = 8;

    if (bol)
      i = 1;
    else if (bol)
      i = 2;
    else if (bol)
      i = 3;
    else if (bol)
      i = 4;
    else if (bol)
      i = 5;
    else if (bol)
      i = 6;
    else if (bol)
      i = 7;
    else if (bol)
      i = 8;

    return i;
  }

  
  int fopt() {
    int i = 0;

    i = 1;
    return i;
  }
}
