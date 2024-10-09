public class AccModuleTest {

    public static void main(String[] args) throws Throwable {
        System.out.println("Test that ACC_MODULE in class access flags does not cause ClassFormatError");
        Class clss = Class.forName("acc_module");
    }
}
