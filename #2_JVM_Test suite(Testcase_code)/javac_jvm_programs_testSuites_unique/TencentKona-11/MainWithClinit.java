

package jdk.test;

public class MainWithClinit {
    static {
        fail();
    }

    private static void fail() {
        throw new RuntimeException("MainWithClinit::<clinit> invoked");
    }

    public static void main(String[] args) {
        System.out.println("hi");
    }
}
