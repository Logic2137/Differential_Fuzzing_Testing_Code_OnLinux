



public class SaveResolutionErrorTest {
    static byte classfile_for_Tester[];
    static byte classfile_for_Loadee[];

    public static void main(java.lang.String[] args) throws Exception {
        ClassLoader appLoader = SaveResolutionErrorTest.class.getClassLoader();
        classfile_for_Tester = appLoader.getResourceAsStream("SaveResolutionErrorTest$Tester.class").readAllBytes();
        classfile_for_Loadee = appLoader.getResourceAsStream("SaveResolutionErrorTest$Loadee.class").readAllBytes();

        long started = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            System.out.println("Test: " + i);
            MyLoader loader = new MyLoader(appLoader);
            loader.doTest();

            if (System.currentTimeMillis() - started > 100000) {
                break;
            }
        }
        System.out.println("Succeed");
    }

    public static class Tester extends Thread {
        static int errorCount = 0;
        synchronized static void incrError() {
            ++ errorCount;
        }

        public volatile static boolean go = false;

        public static void doit() throws Exception {
            System.out.println(Tester.class.getClassLoader());

            Thread t1 = new Tester();
            Thread t2 = new Tester();

            t1.start();
            t2.start();

            go = true;

            t1.join();
            t2.join();


            System.out.println("errorCount = " + errorCount);

            if (errorCount != 0 && errorCount != 2) {
                throw new RuntimeException("errorCount should be 0 or 2 but is " + errorCount);
            }
        }

        static int foobar;
        static boolean debug = false;

        public void run() {
            while (!go) { Thread.onSpinWait(); }

            try {
                
                
                
                
                
                
                
                
                
                

                foobar += Loadee.value;
            } catch (Throwable t) {
                Throwable cause = t.getCause();
                if (cause != null) {
                    String s = cause.toString();
                    if (s.equals(t.toString())) { 
                        t.printStackTrace();
                        throw new RuntimeException("wrong cause");
                    }
                }
                if (debug) {
                    System.out.println(t);
                } else {
                    synchronized (Tester.class) {
                        
                        System.out.println("------");
                        t.printStackTrace();
                        System.out.println("");
                    }
                }
                incrError();
            }
        }
    }

    public static class Loadee {
        static int value = 1234;
    }

    static class MyLoader extends ClassLoader {
        static int count;
        static byte[] badclass = {1, 2, 3, 4, 5, 6, 7, 8};

        static {
            registerAsParallelCapable();
        }

        ClassLoader parent;

        MyLoader(ClassLoader parent) {
            this.parent = parent;
        }

        synchronized boolean hack() {
            ++ count;
            if ((count % 2) == 1) {
                return true;
            } else {
                return false;
            }
        }

        public Class loadClass(String name) throws ClassNotFoundException {
            if (name.equals("SaveResolutionErrorTest$Loadee")) {
                if (hack()) {
                    return defineClass(name, badclass, 0, badclass.length);
                } else {
                    return defineClass(name, classfile_for_Loadee, 0, classfile_for_Loadee.length);
                }
            }
            if (name.equals("SaveResolutionErrorTest$Tester")) {
                return defineClass(name, classfile_for_Tester, 0, classfile_for_Tester.length);
            }
            return parent.loadClass(name);
        }

        void doTest() throws Exception {
            Class c = Class.forName("SaveResolutionErrorTest$Tester", true, this);
            java.lang.reflect.Method m = c.getMethod("doit");
            m.invoke(null);
        }
    }
}
