public class TestParallelAlwaysPreTouch {

    public static void main(String[] args) throws Exception {
        final int M = 1024 * 1024;
        for (int i = 0; i < 10; i++) {
            Object[] obj = new Object[M];
            System.out.println(obj);
        }
    }
}
