


import java.lang.*;

public class LoadLibraryDeadlock {

    public static void main(String[] args) {
        Thread t1 = new Thread() {
            public void run() {
                try {
                    
                    Class<?> c1 = Class.forName("Class1");
                    Object o = c1.newInstance();
                } catch (ClassNotFoundException |
                         InstantiationException |
                         IllegalAccessException e) {
                    System.out.println("Class Class1 not found.");
                    throw new RuntimeException(e);
                }
            }
        };
        Thread t2 = new Thread() {
            public void run() {
                try {
                    
                    Class<?> c2 = Class.forName("p.Class2");
                    System.out.println("Signed jar loaded.");
                } catch (ClassNotFoundException e) {
                    System.out.println("Class Class2 not found.");
                    throw new RuntimeException(e);
                }
            }
        };
        t2.start();
        t1.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ignore) {
        }
    }
}
