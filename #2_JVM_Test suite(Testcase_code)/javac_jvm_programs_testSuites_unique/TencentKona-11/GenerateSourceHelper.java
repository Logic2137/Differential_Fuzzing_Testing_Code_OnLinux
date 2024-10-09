

package nsk.jvmti.RedefineClasses;

import java.util.Random;


public class GenerateSourceHelper {

    static final String STATIC_METHOD_NAME = "staticMethod";
    static final String NONSTATIC_METHOD_NAME = "regularMethod";
    static final String CLASS_NAME = "MyClass";

    private static Random random;

    public static void setRandom(Random random) {
        GenerateSourceHelper.random = random;
    }

    
    static CharSequence generateSource() {
        return "public class " + CLASS_NAME + " { " +
                        "public static String s1 = \"s1s" + random.nextInt() + "dfsdf\"; " +
                                        "public int i = 1345345345; \n" +
                                        "public static double dd = 1e-4; \n" +
                                        "public String s2 = \"s2" + random.nextInt() + "sdfsdf\"; \n" +
                                        "public static String static_s2 = \"s2" + random.nextInt() + "sdfsdf\"; \n" +
                                        "protected String sprotected1 = \"asdfsdf" + random.nextInt() + "sdf\"; \n" +
                                        "protected double d = -.12345; \n" +
                                        "public String methodJustPadding() {return s1 + s2 + d; } \n" +
                                        "public static String " + STATIC_METHOD_NAME + "(double d, int i, Object o) {\n" +
                                                        "String ret_0 = \"little computation \" + (4 * dd  + d + i + o.hashCode() + s1.length()); \n" +
                                                        "String ret_1 = \"in_static_method call_random=" + random.nextInt() + "\"; \n" +
                                                        "String ret =  s1 + static_s2 + d + i + o; \n" +
                                                        
                                                        "return ret; " +
                                        "} \n" +
                                        "public String methodInTheMiddle() {return s1 + s2; } \n" +
                                        "public String " + NONSTATIC_METHOD_NAME + "(double d, int i, Object o) {\n" +
                                                        "String ret_0 = \"little computation \" + (2 * dd + 5 * d  + i + o.hashCode() + s2.length()); \n" +
                                                        "String ret_1 = \"in_nonstatic_method call_random=" + random.nextInt() + "\"; " +
                                                        "String ret = ret_0 + ret_1 +  s1 + s2 + i + o; \n" +
                                                        
                                                        "return ret;" +
                                        "} \n" +
                                        "public String methodFinalInClass() {return s1 + s2 + i; } \n" +
                        "}";
    }


}
