import java.util.concurrent.atomic.*;

public class C1ObjectSpillInLogicOp {

    public static void main(String[] args) {
        AtomicReferenceArray<Integer> x = new AtomicReferenceArray(128);
        Integer y = new Integer(0);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 50000; i++) {
            x.getAndSet(i % x.length(), y);
        }
    }
}
