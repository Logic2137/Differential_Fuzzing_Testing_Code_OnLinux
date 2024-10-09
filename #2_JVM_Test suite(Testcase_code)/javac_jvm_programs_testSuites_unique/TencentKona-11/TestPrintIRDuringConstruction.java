



package compiler.c1;

public class TestPrintIRDuringConstruction {
    static class Dummy {
        public int value;
    }

    static int foo() {
        Dummy obj = new Dummy();       

        obj.value = 0;                 
        return obj.value + obj.value;  
    }

    public static void main(String[] args) {
        for (int i=0; i<5_000; ++i) {
            foo();
        }
    }
 }
