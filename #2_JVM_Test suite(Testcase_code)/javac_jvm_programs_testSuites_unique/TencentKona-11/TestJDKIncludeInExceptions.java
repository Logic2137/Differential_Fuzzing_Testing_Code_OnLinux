

import java.security.Security;


public class TestJDKIncludeInExceptions {

    public static void main(String args[]) throws Exception {
        String incInExc = Security.getProperty("jdk.includeInExceptions");
        if (incInExc != null) {
            throw new RuntimeException("Test failed: default value of " +
                "jdk.includeInExceptions security property is not null: " +
                incInExc);
        }
    }
}
