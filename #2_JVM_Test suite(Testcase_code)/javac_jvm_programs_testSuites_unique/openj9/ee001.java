
package com.ibm.jvmti.tests.eventException;

import java.lang.reflect.Method;

public class ee001 
{
    public native static void invoke(Method m);
    public native static boolean check();
    
    public String helpException()
    {
        return "Test only single exception event gets thrown with JNI frame before handler";
    }
    
    public static void generateException() throws Exception
    {
        throw new Exception();
    }

    public boolean testException() throws Exception
    {
        boolean exceptionCaught = false;
        Method m = ee001.class.getMethod("generateException");
        try {
            invoke(m);
        } catch (Exception e) {
            exceptionCaught = true;
        }

        return exceptionCaught && check();
    }
}
