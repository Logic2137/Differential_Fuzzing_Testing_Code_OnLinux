

class Xost {
    static void log(String msg) { System.out.println(msg); }

    static interface B {
        int ORIGINAL_RETURN = 1;
        int NEW_RETURN = 2;

        private int privateMethod() {
            Runnable race1 = () -> log("Hello from inside privateMethod");
            race1.run();
            return NEW_RETURN;
        }
        public default int defaultMethod(String p) {
            log(p + "from interface B's defaultMethod");
             return privateMethod();
        }
    }

    static class Impl implements B {
    }
}
