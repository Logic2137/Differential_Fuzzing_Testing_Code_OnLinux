
package vm.share.vmstresser;

public class CompileAndDeoptimize implements Runnable {

    public static int v = 0;

    private abstract static class A {
        public abstract void incv();
    }

    private static class B extends A {
        public void incv() {
            v++;
        }
    }

    public static class C extends A {
        public void incv() {
            v += (new int[1][1][1][1][1][1][1][1]).length;
        }
    }

    private volatile boolean done = false;
    public volatile A a = new B();

    private void incv() {
        a.incv();
    }

    private void inc() {
        while ( ! done ) {
            incv();
        }
        
        
        
        
        
        
    }

    public void run() {
        try {
            Thread t = new Thread(new Runnable() { @Override public void run() { inc(); } });
            t.start();
            Thread.sleep(100);
            a = (A) CompileAndDeoptimize.class.getClassLoader().loadClass(B.class.getName().replaceAll("B$", "C")).getConstructors()[0].newInstance(new Object[0]);
            
            
            

        } catch ( Throwable t ) {
            t.printStackTrace();
        }
    }

}
