



package compiler.c2;



public class TestCharShortByteSwap {

  private static short initShort(String[] args, short v) {
    if (args.length > 0) {
      try {
        return (short) Integer.valueOf(args[0]).intValue();
      } catch (NumberFormatException e) { }
    }
    return v;
  }

  private static char initChar(String[] args, char v) {
    if (args.length > 0) {
      try {
        return (char) Integer.valueOf(args[0]).intValue();
      } catch (NumberFormatException e) { }
    }
    return v;
  }

  private static void testChar(char a, char b) {
    if (a != Character.reverseBytes(b)) {
      throw new RuntimeException("FAIL: " + (int)a + " != Character.reverseBytes(" + (int)b + ")");
    }
    if (b != Character.reverseBytes(a)) {
      throw new RuntimeException("FAIL: " + (int)b + " != Character.reverseBytes(" + (int)a + ")");
    }
  }

  private static void testShort(short a, short b) {
    if (a != Short.reverseBytes(b)) {
      throw new RuntimeException("FAIL: " + (int)a + " != Short.reverseBytes(" + (int)b + ")");
    }
    if (b != Short.reverseBytes(a)) {
      throw new RuntimeException("FAIL: " + (int)b + " != Short.reverseBytes(" + (int)a + ")");
    }
  }

  public static void main(String[] args) {
    for (int i = 0; i < 100000; ++i) { 
      char c1 = initChar(args, (char) 0x0123);
      char c2 = initChar(args, (char) 0x2301);
      char c3 = initChar(args, (char) 0xaabb);
      char c4 = initChar(args, (char) 0xbbaa);
      short s1 = initShort(args, (short) 0x0123);
      short s2 = initShort(args, (short) 0x2301);
      short s3 = initShort(args, (short) 0xaabb);
      short s4 = initShort(args, (short) 0xbbaa);
      testChar(c1, c2);
      testChar(c3, c4);
      testShort(s1, s2);
      testShort(s3, s4);
    }
  }
}
