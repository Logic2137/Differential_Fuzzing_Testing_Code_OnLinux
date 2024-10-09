public class IntRotateWithImmediate {

    static int rotateRight1(int i, int distance) {
        return ((i >>> distance) | (i << -distance));
    }

    static int rotateRight2(int i, int distance) {
        return ((i >>> distance) | (i << (32 - distance)));
    }

    static int compute1(int x) {
        return rotateRight1(x, 3);
    }

    static int compute2(int x) {
        return rotateRight2(x, 3);
    }

    public static void main(String[] args) {
        int val = 4096;
        int firstResult = compute1(val);
        for (int i = 0; i < 100000; i++) {
            int newResult = compute1(val);
            if (firstResult != newResult) {
                throw new InternalError(firstResult + " != " + newResult);
            }
            newResult = compute2(val);
            if (firstResult != newResult) {
                throw new InternalError(firstResult + " != " + newResult);
            }
        }
        System.out.println("OK");
    }
}
