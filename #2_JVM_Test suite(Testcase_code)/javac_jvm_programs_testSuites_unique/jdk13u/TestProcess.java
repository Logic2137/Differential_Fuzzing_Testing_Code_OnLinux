
package test;

public class TestProcess {

    public static void main(String[] args) throws Exception {
        System.out.print("The process started, pid:" + ProcessHandle.current().pid());
        while (true) {
            Thread.sleep(100);
        }
    }
}
