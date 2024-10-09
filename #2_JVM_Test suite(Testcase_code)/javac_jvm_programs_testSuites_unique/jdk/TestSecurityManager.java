



public class TestSecurityManager extends SecurityManager {
    public static final int EXIT_VALUE = 123;

    public TestSecurityManager() {
    }

    public void checkListen(int port) {
        
        
        
        
        
        
        
        System.exit(EXIT_VALUE);
    }

    public void checkExit(int status) {
        
    }
}
