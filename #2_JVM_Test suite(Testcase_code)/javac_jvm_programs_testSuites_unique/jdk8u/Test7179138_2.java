public class Test7179138_2 {

    public static void main(String[] args) throws Exception {
        System.out.println("Java Version: " + System.getProperty("java.vm.version"));
        long[] durations = new long[60];
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 100000; i++) {
            for (long duration : durations) {
            }
            {
                String s = "test";
                int len = s.length();
                s = s + s;
                len = len + len;
                s = s + s;
                len = len + len;
                s = s + s;
                len = len + len;
                if (s.length() != len) {
                    System.out.println("Failed at iteration: " + i);
                    System.out.println("Length mismatch: " + s.length() + " <> " + len);
                    System.out.println("Expected: \"" + "test" + "test" + "test" + "test" + "test" + "test" + "test" + "test" + "\"");
                    System.out.println("Actual:   \"" + s + "\"");
                    System.exit(0);
                }
            }
        }
    }
}
