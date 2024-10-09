

package compiler.arraycopy;
import java.util.Random;



public class TestArrayCopyConjoint {

   public static final int SIZE = 4096;
   public static byte[] fromByteArr, toByteArr, valByteArr;
   public static char[] fromCharArr, toCharArr, valCharArr;
   public static int[] fromIntArr, toIntArr, valIntArr;
   public static long[] fromLongArr, toLongArr, valLongArr;

   static public  void reinit(Class<?> c) {
     if (c == byte.class) {
       for (int i = 0 ; i < SIZE ; i++) {
         fromByteArr[i] = (byte)i;
       }
     } else if (c == char.class) {
       for (int i = 0 ; i < SIZE ; i++) {
         fromCharArr[i] = (char)i;
       }
     } else if (c == int.class) {
       for (int i = 0 ; i < SIZE ; i++) {
         fromIntArr[i]  = i;
       }
     } else {
       assert c == long.class;
       for (int i = 0 ; i < SIZE ; i++) {
         fromLongArr[i] = i;
       }
     }
   }

   static public void setup() {
     
     fromByteArr = new byte[SIZE];
     valByteArr  = new byte[SIZE];
     toByteArr = fromByteArr;
     fromCharArr = new char[SIZE];
     valCharArr  = new char[SIZE];
     toCharArr = fromCharArr;
     fromIntArr = new int[SIZE];
     valIntArr  = new int[SIZE];
     toIntArr = fromIntArr;
     fromLongArr = new long[SIZE];
     valLongArr  = new long[SIZE];
     toLongArr = fromLongArr;

     for (int i = 0 ; i < SIZE ; i++) {
        fromByteArr[i] = (byte)i;
        valByteArr[i] = (byte)i;
        fromCharArr[i] = (char)i;
        valCharArr[i] = (char)i;
        fromIntArr[i]  = i;
        valIntArr[i]  = i;
        fromLongArr[i] = i;
        valLongArr[i] = i;
     }
    }

    public static int validate_ctr = 0;
    public static <E> void validate(String msg, E arr, int length, int fromPos, int toPos) {
      validate_ctr++;
      if (arr instanceof byte [])  {
        byte [] barr = (byte [])arr;
        for(int i = 0 ; i < length; i++)
          if (valByteArr[i+fromPos] != barr[i+toPos]) {
             System.out.println(msg + "[" + arr.getClass() + "] Result mismtach at i = " + i
                                + " expected = " + valByteArr[i+fromPos]
                                + " actual   = " + barr[i+toPos]
                                + " fromPos = " + fromPos
                                + "  toPos = " + toPos);
             throw new Error("Fail");

          }
      }
      else if (arr instanceof char [])  {
        char [] carr = (char [])arr;
        for(int i = 0 ; i < length; i++)
          if (valCharArr[i+fromPos] != carr[i+toPos]) {
             System.out.println(msg + "[" + arr.getClass() + "] Result mismtach at i = " + i
                                + " expected = " + valCharArr[i+fromPos]
                                + " actual   = " + carr[i+toPos]
                                + " fromPos = " + fromPos
                                + " toPos = " + toPos);
             throw new Error("Fail");
          }
      }
      else if (arr instanceof int [])  {
        int [] iarr = (int [])arr;
        for(int i = 0 ; i < length; i++)
          if (valIntArr[i+fromPos] != iarr[i+toPos]) {
             System.out.println(msg + "[" + arr.getClass() + "] Result mismtach at i = " + i
                                + " expected = " + valIntArr[i+fromPos]
                                + " actual   = " + iarr[i+toPos]
                                + " fromPos = " + fromPos
                                + " toPos = " + toPos);
             throw new Error("Fail");
          }
      }
      else if (arr instanceof long [])  {
        long [] larr = (long [])arr;
        for(int i = 0 ; i < length; i++)
          if (valLongArr[i+fromPos] != larr[i+toPos]) {
             System.out.println(msg + "[" + arr.getClass() + "] Result mismtach at i = " + i
                                + " expected = " + valLongArr[i+fromPos]
                                + " actual   = " + larr[i+toPos]
                                + " fromPos = " + fromPos
                                + " toPos = " + toPos);
             throw new Error("Fail");
          }
      }
    }

    public static void testByte(int length, int fromPos, int toPos) {
       System.arraycopy(fromByteArr, fromPos, toByteArr, toPos, length);
       validate(" Test ByteArr ", toByteArr, length, fromPos, toPos);
    }

    public static void testChar(int length, int fromPos, int toPos) {
       System.arraycopy(fromCharArr, fromPos, toCharArr, toPos, length);
       validate(" Test CharArr ", toCharArr, length, fromPos, toPos);
    }

    public static void testInt(int length, int fromPos, int toPos) {
       System.arraycopy(fromIntArr, fromPos, toIntArr, toPos, length);
       validate(" Test IntArr ", toIntArr, length, fromPos, toPos);
    }

    public static void testLong(int length, int fromPos, int toPos) {
       System.arraycopy(fromLongArr, fromPos, toLongArr, toPos, length);
       validate(" Test LongArr ", toLongArr, length, fromPos, toPos);
    }

    public static void testByte_constant_LT32B(int fromPos, int toPos) {
       System.arraycopy(fromByteArr, fromPos, toByteArr, toPos, 7);
       validate(" Test Byte constant length 7 ", toByteArr, 7, fromPos, toPos);
    }
    public static void testByte_constant_LT64B(int fromPos, int toPos) {
       System.arraycopy(fromByteArr, fromPos, toByteArr, toPos, 45);
       validate(" Test Byte constant length 45 ", toByteArr, 45, fromPos, toPos);
    }

    public static void testChar_constant_LT32B(int fromPos, int toPos) {
       System.arraycopy(fromCharArr, fromPos, toCharArr, toPos, 7);
       validate(" Test Char constant length 7 ", toCharArr, 7, fromPos, toPos);
    }
    public static void testChar_constant_LT64B(int fromPos, int toPos) {
       System.arraycopy(fromCharArr, fromPos, toCharArr, toPos, 22);
       validate(" Test Char constant length 22 ", toCharArr, 22, fromPos, toPos);
    }

    public static void testInt_constant_LT32B(int fromPos, int toPos) {
       System.arraycopy(fromIntArr, fromPos, toIntArr, toPos, 7);
       validate(" Test Int constant length 7 ", toIntArr, 7, fromPos, toPos);
    }
    public static void testInt_constant_LT64B(int fromPos, int toPos) {
       System.arraycopy(fromIntArr, fromPos, toIntArr, toPos, 11);
       validate(" Test Int constant length 11 ", toIntArr, 11, fromPos, toPos);
    }

    public static void testLong_constant_LT32B(int fromPos, int toPos) {
       System.arraycopy(fromLongArr, fromPos, toLongArr, toPos, 3);
       validate(" Test Long constant length 3 ", toLongArr, 3, fromPos, toPos);
    }
    public static void testLong_constant_LT64B(int fromPos, int toPos) {
       System.arraycopy(fromLongArr, fromPos, toLongArr, toPos, 6);
       validate(" Test Long constant length 6 ", toLongArr, 6, fromPos, toPos);
    }


    public static void main(String [] args) {
      
      
      
      
      int [] lengths =  {   29,    59,    89,    125,     159,      189,   194,   1024 };
      Random r = new Random(1024);

      setup();

      try {
        for (int i = 0 ; i < 1000000 ; i++ ) {
          int index = r.nextInt(2048);
          testByte(lengths[i % lengths.length], index , index+2);
          reinit(byte.class);
          testByte_constant_LT32B (index , index+2);
          reinit(byte.class);
          testByte_constant_LT64B (index , index+2);
          reinit(byte.class);

          testChar(lengths[i % lengths.length] >> 1, index , index+2);
          reinit(char.class);
          testChar_constant_LT32B (index , index+2);
          reinit(char.class);
          testChar_constant_LT64B (index , index+2);
          reinit(char.class);

          testInt(lengths[i % lengths.length]  >> 2, index , index+2);
          reinit(int.class);
          testInt_constant_LT32B (index , index+2);
          reinit(int.class);
          testInt_constant_LT64B (index , index+2);
          reinit(int.class);

          testLong(lengths[i % lengths.length] >> 3, index , index+2);
          reinit(long.class);
          testLong_constant_LT32B (index , index+2);
          reinit(long.class);
          testLong_constant_LT64B (index , index+2);
          reinit(long.class);
        }
        System.out.println("PASS : " + validate_ctr);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
}
