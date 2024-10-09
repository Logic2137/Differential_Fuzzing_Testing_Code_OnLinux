



import java.lang.reflect.Field;


public class FastGetField {

    private static final String agentLib = "FastGetField";

    private native boolean initFieldIDs(Class c);
    private native boolean initWatchers(Class c);
    public native long accessFields(MyItem i);
    public static native long getFieldAccessCount();

    static final int loop_cnt = 10000;


    class MyItem {
        
        boolean Z;
        byte B;
        short S;
        char C;
        int I;
        long J;
        float F;
        double D;

        public void change_values() {
            Z = true;
            B = 1;
            C = 1;
            S = 1;
            I = 1;
            J = 1l;
            F = 1.0f;
            D = 1.0;
        }

        public void reset_values() {
            Z = false;
            B = 0;
            C = 0;
            S = 0;
            I = 0;
            J = 0l;
            F = 0.0f;
            D = 0.0;
        }
    }

    
    static {
        try {
            System.loadLibrary(agentLib);
        } catch (UnsatisfiedLinkError ex) {
            System.err.println("Failed to load " + agentLib + " lib");
            System.err.println("java.library.path: " + System.getProperty("java.library.path"));
            throw ex;
        }
    }

    public void TestFieldAccess() throws Exception {
        MyItem i = new MyItem();
        if (!initFieldIDs(MyItem.class)) throw new RuntimeException("FieldID initialization failed!");

        long duration = System.nanoTime();
        for (int c = 0; c < loop_cnt; ++c) {
            if (accessFields(i) != 0l) throw new RuntimeException("Wrong initial result!");
            i.change_values();
            if (accessFields(i) != 8l) throw new RuntimeException("Wrong result after changing!");
            i.reset_values();
        }
        duration = System.nanoTime() - duration;
        System.out.println(loop_cnt + " iterations took " + duration + "ns.");

        if (getFieldAccessCount() != 0) throw new RuntimeException("Watch not yet active!");

        
        if (!initWatchers(MyItem.class)) throw new RuntimeException("JVMTI missing!");

        
        if (accessFields(i) != 0l) throw new RuntimeException("Wrong initial result!");
        i.change_values();
        if (accessFields(i) != 8l) throw new RuntimeException("Wrong result after changing!");
        if (getFieldAccessCount() != 16) throw new RuntimeException("Unexpected event count!");
    }

    public static void main(String[] args) throws Exception {
        FastGetField inst = new FastGetField();
        inst.TestFieldAccess();
    }
}
