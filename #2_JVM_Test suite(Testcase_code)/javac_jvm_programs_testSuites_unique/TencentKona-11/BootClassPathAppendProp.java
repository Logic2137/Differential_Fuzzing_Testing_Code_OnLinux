

import java.io.File;





public class BootClassPathAppendProp {
    public static void main(String[] args) throws Exception {
        
        
        if (System.getProperty("jdk.boot.class.path.append") != null) {
            throw new RuntimeException("Test failed, jdk.boot.class.path.append has value: " +
                System.getProperty("jdk.boot.class.path.append"));
        } else {
            System.out.println("Test BootClassPathAppendProp passed");
        }
    }
}
