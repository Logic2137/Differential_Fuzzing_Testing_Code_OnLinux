class TestDeletedMethod_Super {

    public static final int ID = 2;

    private static int m() {
        System.out.println("Super.m");
        return ID;
    }
}

class TestDeletedMethod_Sub extends TestDeletedMethod_Super {

    public static final int ID = 1;

    private static int m() {
        System.out.println("Sub.m");
        return ID;
    }

    public static int test() {
        return TestDeletedMethod_Sub.m();
    }
}

public class TestDeletedMethod {

    public static void main(String[] args) {
        int x = TestDeletedMethod_Sub.test();
        if (x != TestDeletedMethod_Super.ID)
            throw new RuntimeException("Wrong method invoked: " + x);
    }
}
