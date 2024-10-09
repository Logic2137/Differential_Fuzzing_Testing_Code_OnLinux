
package gc.memory.Array.SampleMe;

public class SampleClass {

    public void foo() {
        System.out.println("gronk, gronk, gronk");
        int i = (int) (Math.random() * 1000);
        int j = i * 5;
        System.out.println(i + "" + j);
    }

    static int r;

    int joop;

    String masto;

    Judd j;

    public SampleClass() {
        j = new Judd();
    }
}

class Judd {

    Object[] oo;

    public Judd() {
        oo = new Object[100];
        for (int i = 0; i < 100; i++) {
            oo[i] = new Object();
        }
    }
}
