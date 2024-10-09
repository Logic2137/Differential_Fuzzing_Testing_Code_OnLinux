public class TestRemoveEmptyLoop {

    public void test() {
        int i = 34;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (; i > 0; i -= 11) ;
        if (i < 0) {
        } else {
            throw new RuntimeException("Test failed.");
        }
    }

    public static void main(String[] args) {
        TestRemoveEmptyLoop _instance = new TestRemoveEmptyLoop();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 50000; i++) {
            _instance.test();
        }
        System.out.println("Test passed.");
    }
}
