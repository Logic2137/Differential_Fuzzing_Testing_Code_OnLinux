

class TestMT {

    static boolean loadLib(String libName) {
        try {
            System.loadLibrary(libName);
            System.out.println("Loaded library "+ libName + ".");
            return true;
        } catch (SecurityException e) {
            System.out.println("loadLibrary(\"" + libName + "\") throws: " + e + "\n");
        } catch (UnsatisfiedLinkError e) {
            System.out.println("loadLibrary(\"" + libName + "\") throws: " + e + "\n");
        }
        return false;
    }

    public static int counter = 1;
    static int Runner() {
        counter = counter * -1;
        int i = counter;
        if (counter < 2) counter += Runner();
        return i;
    }

    public static int run(String msg) {
        try {
            Runner();
        } catch (StackOverflowError e) {
            System.out.println(msg + " caught stack overflow error.");
            return 0;
        } catch (OutOfMemoryError e) {
            return 0;
        }
        return 2;
    }

    public static void main(String argv[]) {
        try {
            for (int i = 0; i < 20; i++) {
                Thread t = new DoStackOverflow("SpawnedThread " + i);
                t.start();
            }
            run("Main thread");
            loadLib("test-rwx");
            run("Main thread");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static class DoStackOverflow extends Thread {
        public DoStackOverflow(String name) {
            super(name);
        }
        public void run() {
            for (int i = 0; i < 10; ++i) {
                TestMT.run(getName());
                yield();
            }
        }
    }
}
