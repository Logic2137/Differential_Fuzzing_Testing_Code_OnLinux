



package vm.compiler.jbe.hoist.hoist03;






public class hoist03 {
    int LEN = 60000;
    int a[] = new int[LEN];
    int aopt[] = new int[LEN];

    public static void main(String args[]) {
        hoist03 hst = new hoist03();

        hst.f();
        hst.fopt();
        if (hst.eCheck()) {
            System.out.println("Test hoist03 Passed.");
        } else {
            throw new Error("Test hoist03 Failed.");
        }
    }

    void f() {
        
        int k = 0;

        for (int j = 0; j < 200; j++) {
            for (int i = 0; i < 300; i++) {
                if (i < 0) {
                    a[k++] = -1;
                }
                else {
                    a[k++] = 1;
                }
            }
        }
    }


    
    void fopt() {
        int k = 0;

        for (int j = 0; j < 200; j++) {
            for (int i = 0; i < 300; i++) {
                aopt[k++] = 1;
            }
        }
    }


    
    boolean eCheck() {
        for (int i = 0; i < a.length; i++)
            if (a[i] != aopt[i]) {
                System.out.println("a["+i+"]="+a[i]+"; aopt["+i+"]="+aopt[i]);
                return false;
            }

        return true;
    }
}
