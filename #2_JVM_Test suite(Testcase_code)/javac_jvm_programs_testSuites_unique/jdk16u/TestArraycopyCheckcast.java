

package gc.epsilon;



public class TestArraycopyCheckcast {

  static int COUNT = Integer.getInteger("count", 1000);

  public static void main(String[] args) throws Exception {
    Object[] src = new Object[COUNT];
    Object[] dst = new B[COUNT];

    
    try {
      System.arraycopy(src, 0, dst, 0, COUNT);
    } catch (ArrayStoreException e) {
      throw new IllegalStateException("Should have completed");
    }

    
    for (int c = 0; c < COUNT; c++) {
      src[c] = new A();
    }

    try {
      System.arraycopy(src, 0, dst, 0, COUNT);
      throw new IllegalStateException("Should have failed with ArrayStoreException");
    } catch (ArrayStoreException e) {
      
    }

    
    for (int c = 0; c < COUNT; c++) {
      src[c] = new C();
    }

    try {
      System.arraycopy(src, 0, dst, 0, COUNT);
    } catch (ArrayStoreException e) {
      throw new IllegalStateException("Should have completed");
    }

    for (int c = 0; c < COUNT; c++) {
      if (src[c] != dst[c]) {
        throw new IllegalStateException("Copy failed at index " + c);
      }
    }
  }

  static class A {}
  static class B extends A {}
  static class C extends B {}
}
