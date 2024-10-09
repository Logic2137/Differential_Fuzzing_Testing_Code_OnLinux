@FunctionalInterface
interface MyInterface {

    public String sayHello();
}

class MyClass implements MyInterface {

    public String sayHello() {
        return "Hello";
    }
}

public class SimpleApp {

    public static boolean useLambda;

    public static void main(String[] args) {
        useLambda = (args.length == 1 && args[0].equals("lambda")) ? true : false;
        if (!useLambda) {
            MyClass mc = new MyClass();
            System.out.println(mc.sayHello());
        } else {
            MyInterface msg = () -> {
                return "Hello";
            };
            System.out.println(msg.sayHello());
        }
    }
}
