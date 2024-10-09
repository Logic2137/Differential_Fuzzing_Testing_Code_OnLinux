



package compiler.escapeAnalysis;

public class TestIdealAllocShape {
    static volatile DataA f1;

    static class DataA {
        DataA f1;
        DataA(DataA p1) {
            this.f1 = p1;
        }
        public void m1() {}
    }

    public static DataA test1() {
        DataA l1 = new DataA(null);
        for (int i=0; i<2; i++) {
            try {
                return new DataA(l1); 
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static DataA test2() {
        DataA l1 = new DataA(null);
        for (int i=0; i<2; i++) {
            try {
                f1 = new DataA(l1); 
                break; 
            } catch (Exception e) {
            }
        }
        synchronized(l1) { 
            l1.m1();
        }
        return null;
    }

    public static void main(String argv[]) {

        for (int i=0; i<20000; i++) {
            TestIdealAllocShape.test1();
        }

        for (int i=0; i<20000; i++) {
            TestIdealAllocShape.test2();
        }
    }
}
