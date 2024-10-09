


import java.io.*;
import java.lang.reflect.Array;

public class ArrayGetIntException {
    public static void main(String[] args) throws Exception {
        Object[] objArray = {Integer.valueOf(Integer.MAX_VALUE)};

        
        try {
            System.out.println(Array.get(objArray, 0));
            System.out.println("Test #1 PASSES");
        } catch(Exception e) {
            failTest("Test #1 FAILS - legal access denied" + e.getMessage());
        }

        
        try {
            System.out.println(Array.getInt(objArray, 0));
            failTest("Test #2 FAILS - no exception");
        } catch(Exception e) {
            System.out.println(e);
            if (e.getMessage().equals("Argument is not an array of primitive type")) {
                System.out.println("Test #2 PASSES");
            } else {
                failTest("Test #2 FAILS - incorrect message: " + e.getMessage());
            }
        }

        
        try {
            System.out.println(Array.getInt(new Object(), 0));
            failTest("Test #3 FAILS - no exception");
        } catch(Exception e) {
            System.out.println(e);
            if (e.getMessage().equals("Argument is not an array")) {
                System.out.println("Test #3 PASSES");
            } else {
                failTest("Test #3 FAILS - incorrect message: " + e.getMessage());
            }
        }
    }

    private static void failTest(String errStr) {
        System.out.println(errStr);
        throw new Error(errStr);
    }
}
