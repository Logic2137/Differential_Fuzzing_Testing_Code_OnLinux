



import java.util.concurrent.atomic.*;
public class C1ObjectSpillInLogicOp {
  static public void main(String[] args) {
    AtomicReferenceArray<Integer> x = new AtomicReferenceArray(128);
    Integer y = new Integer(0);
    for (int i = 0; i < 50000; i++) {
      x.getAndSet(i % x.length(), y);
    }
  }
}
