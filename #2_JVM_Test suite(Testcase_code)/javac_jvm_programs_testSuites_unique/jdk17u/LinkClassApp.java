class Parent {

    int get() {
        return 1;
    }
}

class Child extends Parent {

    int get() {
        return 2;
    }

    int dummy() {
        Parent2 x = new Child2();
        return x.get();
    }
}

class Parent2 {

    int get() {
        return 3;
    }
}

class Child2 extends Parent2 {

    int get() {
        return 4;
    }
}

class MyShutdown extends Thread {

    public void run() {
        System.out.println("shut down hook invoked...");
    }
}

class LinkClassApp {

    public static void main(String[] args) {
        Runtime r = Runtime.getRuntime();
        r.addShutdownHook(new MyShutdown());
        if (args.length > 0 && args[0].equals("run")) {
            System.out.println("test() = " + test());
        } else {
            System.out.println("Test.class is initialized.");
            System.out.println("Parent.class and Child.class are loaded when Test.class is verified,");
            System.out.println("but these two classes are not linked");
        }
        if (args.length > 0 && args[0].equals("callExit")) {
            System.exit(0);
        }
    }

    static int test() {
        Parent x = new Child();
        return x.get();
    }
}
