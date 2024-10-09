public class Test6753639 {

    public static void main(String[] args) throws InterruptedException {
        int END = Integer.MAX_VALUE;
        int count = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = Integer.MAX_VALUE - 5; i <= END; i++) {
            count++;
            if (count > 100000) {
                System.out.println("Passed");
                System.exit(95);
            }
        }
        System.out.println("broken " + count);
        System.out.println("FAILED");
        System.exit(97);
    }
}
