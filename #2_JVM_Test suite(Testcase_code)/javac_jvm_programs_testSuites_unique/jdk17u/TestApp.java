import java.io.IOException;

public class TestApp {

    public static void doSomething() throws IOException {
        int r = System.in.read();
        System.out.println("read: " + r);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("main enter");
        System.out.flush();
        doSomething();
        System.out.println("main exit");
    }
}
