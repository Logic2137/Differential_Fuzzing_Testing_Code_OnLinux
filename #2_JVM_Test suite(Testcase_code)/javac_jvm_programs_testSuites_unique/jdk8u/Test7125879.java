public class Test7125879 {

    String var_1 = "abc";

    public Test7125879() {
        var_1 = var_1.replaceAll("d", "e") + var_1;
    }

    public static void main(String[] args) {
        Test7125879 t = new Test7125879();
        try {
            t.test();
        } catch (Throwable e) {
        }
    }

    private void test() {
        new Test7125879().var_1 = ((Test7125879) (new Object[-1])[0]).var_1;
    }
}
