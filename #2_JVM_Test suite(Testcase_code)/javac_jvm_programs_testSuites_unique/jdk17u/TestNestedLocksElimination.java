import java.util.LinkedList;

public class TestNestedLocksElimination {

    private LinkedList<char[]> buffers = new LinkedList<>();

    private boolean complete = false;

    private int bufferSize;

    void foo(char[] ca) {
    }

    char[] getNext(int length, int count) {
        if (this.buffers.isEmpty()) {
            return new char[100];
        }
        char[] b = (char[]) this.buffers.getFirst();
        if (count >= 100) {
            this.complete = true;
            this.buffers.clear();
        }
        return b;
    }

    synchronized boolean isComplete() {
        return this.complete;
    }

    synchronized boolean availableSegment() {
        return (buffers.isEmpty() == false);
    }

    TestNestedLocksElimination getHolder(TestNestedLocksElimination s1, TestNestedLocksElimination s2, int count) {
        return (count & 7) == 0 ? s2 : s1;
    }

    int test(TestNestedLocksElimination s1, TestNestedLocksElimination s2, int maxToSend) {
        boolean isComplete = true;
        boolean availableSegment = false;
        int size = 0;
        int count = 0;
        do {
            TestNestedLocksElimination s = getHolder(s1, s2, count++);
            synchronized (s) {
                isComplete = s.isComplete();
                availableSegment = s.availableSegment();
            }
            synchronized (this) {
                size = 0;
                while (size < maxToSend) {
                    char[] b = null;
                    synchronized (s) {
                        b = s.getNext(maxToSend - size, count);
                        isComplete = s.isComplete();
                        availableSegment = s.availableSegment();
                    }
                    foo(b);
                    size += b.length;
                }
            }
        } while (availableSegment == true || isComplete == false);
        return size;
    }

    public static void main(String[] args) {
        int count = 0;
        int n = 0;
        TestNestedLocksElimination t = new TestNestedLocksElimination();
        TestNestedLocksElimination s1 = new TestNestedLocksElimination();
        TestNestedLocksElimination s2 = new TestNestedLocksEliminationSub();
        char[] c = new char[100];
        while (n++ < 20_000) {
            s1.buffers.add(c);
            s2.buffers.add(c);
            count += t.test(s1, s2, 10000);
        }
        System.out.println(" count: " + count);
    }
}

class TestNestedLocksEliminationSub extends TestNestedLocksElimination {

    public boolean isComplete() {
        return true;
    }
}
