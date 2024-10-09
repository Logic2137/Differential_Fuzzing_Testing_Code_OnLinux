
package com.ibm.jvmti.tests.iterateThroughHeap;

public class ith001Sub 
{
    boolean testBooleanTrue = true;
    byte    testByte  = 0x44;
    char    testChar  = 0x55;
    short   testShort = 0x5566;
    int     testInt   = 0x66667777;
    long    testLong  = 0xc0fec0dedeadbeefL;
    float   testFloat = -10;
    double  testDouble = -20.0;


    static boolean testBooleanArray[] = { true, false, true, false, true, true, false, false };
    static char testCharArray[]     = { 'a', 'b', 'c', 'd' };
    static byte testByteArray[]     = { 0x11, 0x22, 0x33, 0x44 };
    static short testShortArray[]   = { 0x1000, 0x2000, 0x3000, 0x4000 };
    static int testIntArray[]       = { 0x10000, 0x20000, 0x30000, 0x40000 };
    static long testLongArray[]     = { 0x100000, 0x200000, 0x300000, 0x400000 };
    static float testFloatArray[]   = { 1, 2, 3, 4, -1, -2, -3, -4 };
    static double testDoubleArray[] = { 1.0, 2.0, 3.0, 4.0, -1.0, -2.0, -3.0, -4.0 };

    static boolean testBoolean2DArray[][] = { {true, true}, {false, false} };

    String testString = new String("testStringContent");  
     
    public static native boolean testHeapIteration();
    
    public static native boolean testArrayPrimitive();
    
    public static native boolean testFieldPrimitive();
    
    public static native boolean testStringPrimitive();
    
    public static native boolean tagObject(long tag, Object o);
    
    public void
    testArrayPrimitive_tagArrays()
    {
    	tagObject(0xc0de0001, testBooleanArray);
    	tagObject(0xc0de0002, testCharArray);
    	tagObject(0xc0de0003, testByteArray);
    	tagObject(0xc0de0004, testShortArray);
    	tagObject(0xc0de0005, testIntArray);
    	tagObject(0xc0de0006, testLongArray);
    	tagObject(0xc0de0007, testFloatArray);
    	tagObject(0xc0de0008, testDoubleArray);
    	tagObject(0xc0de0009, testBoolean2DArray);
    	
    }
    
}
