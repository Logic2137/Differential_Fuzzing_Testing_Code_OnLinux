



package compiler.onSpinWait;

public class TestOnSpinWaitEnableDisable {
    public static void main(String[] args) {
        for (int i = 0; i < 50_000; i++) {
            java.lang.Thread.onSpinWait();
        }
    }
}
