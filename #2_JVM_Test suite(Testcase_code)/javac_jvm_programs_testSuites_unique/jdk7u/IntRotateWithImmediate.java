



public class IntRotateWithImmediate {

  
  static int rotateRight(int i, int distance) {
    
    
    
    
    
    
    return ((i >>> distance) | (i << -distance));
  }

  static int compute(int x) {
    return rotateRight(x, 3);
  }

  public static void main(String args[]) {
    int val = 4096;

    int firstResult = compute(val);

    for (int i = 0; i < 100000; i++) {
      int newResult = compute(val);
      if (firstResult != newResult) {
        throw new InternalError(firstResult + " != " + newResult);
      }
    }
    System.out.println("OK");
  }

}
