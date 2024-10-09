
package optionsvalidation;

import java.lang.ref.WeakReference;

class JVMStartup {

    private static volatile WeakReference<Object> weakRef;

    private static synchronized void createWeakRef() {
        Object o = new Object();
        weakRef = new WeakReference<>(o);
    }

    public static void main(String[] args) throws Exception {
        byte[] garbage = new byte[8192];
        int i = 0;
        createWeakRef();
        do {
            garbage = new byte[8192];
            i++;
            if (i > 5) {
                System.gc();
            }
        } while (weakRef.get() != null);
        System.out.println("Java start-up!");
    }
}
