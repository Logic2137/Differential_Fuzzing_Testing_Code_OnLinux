



public abstract class Expr {

    public static class MemI {
        public MemI(int i) {
            this.value = i;
        }

        public int value;
    }

    public static class MemL {
        public MemL(long l) {
            this.value = l;
        }

        public long value;
    }

    public boolean isUnaryArgumentSupported() {
        return false;
    }

    public boolean isIntExprSupported() {
        return false;
    }

    public boolean isBinaryArgumentSupported() {
        return false;
    }

    public boolean isLongExprSupported() {
        return false;
    }

    public boolean isMemExprSupported() {
        return false;
    }

    public int intExpr(int reg) {
        throw new UnsupportedOperationException();
    }

    public int intExpr(MemI mem) {
        throw new UnsupportedOperationException();
    }

    public int intExpr(int a, int b) {
        throw new UnsupportedOperationException();
    }

    public int intExpr(int a, MemI b) {
        throw new UnsupportedOperationException();
    }

    public int intExpr(MemI a, int b) {
        throw new UnsupportedOperationException();
    }

    public int intExpr(MemI a, MemI b) {
        throw new UnsupportedOperationException();
    }

    public long longExpr(long reg) {
        throw new UnsupportedOperationException();
    }

    public long longExpr(MemL mem) {
        throw new UnsupportedOperationException();
    }

    public long longExpr(long a, long b) {
        throw new UnsupportedOperationException();
    }

    public long longExpr(long a, MemL b) {
        throw new UnsupportedOperationException();
    }

    public long longExpr(MemL a, long b) {
        throw new UnsupportedOperationException();
    }

    public long longExpr(MemL a, MemL b) {
        throw new UnsupportedOperationException();
    }

    public static class BMIExpr extends Expr {

        public boolean isMemExprSupported() {
            return true;
        }
    }

    public static class BMIBinaryExpr extends BMIExpr {

        public boolean isBinaryArgumentSupported() {
            return true;
        }

    }

    public static class BMIUnaryExpr extends BMIExpr {
        public boolean isUnaryArgumentSupported() {
            return true;
        }
    }

    public static class BMIBinaryIntExpr extends BMIBinaryExpr {
        public boolean isIntExprSupported() {
            return true;
        }
    }

    public static class BMIBinaryLongExpr extends BMIBinaryExpr {
        public boolean isLongExprSupported() {
            return true;
        }
    }

    public static class BMIUnaryIntExpr extends BMIUnaryExpr {
        public boolean isIntExprSupported() {
            return true;
        }
    }

    public static class BMIUnaryLongExpr extends BMIUnaryExpr {
        public boolean isLongExprSupported() {
            return true;
        }
    }

    public static class BitCountingExpr extends Expr {
        public boolean isUnaryArgumentSupported() {
            return true;
        }
    }

    public static class BitCountingIntExpr extends BitCountingExpr {
        public boolean isIntExprSupported() {
            return true;
        }
    }

    public static class BitCountingLongExpr extends BitCountingExpr {
        public boolean isLongExprSupported() {
            return true;
        }
    }
}
