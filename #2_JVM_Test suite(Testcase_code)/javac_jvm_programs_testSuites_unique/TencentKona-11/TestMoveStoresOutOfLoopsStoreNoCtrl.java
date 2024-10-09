



package compiler.loopopts;

public class TestMoveStoresOutOfLoopsStoreNoCtrl {

    static void test(boolean flag) {
        for (int i = 0; i < 20000; i++) {
            if (flag) {
                int j = 0;
                do {
                    j++;
                } while(j < 10);
            }
        }
    }

    static public void main(String[] args) {
        test(false);
    }

}
