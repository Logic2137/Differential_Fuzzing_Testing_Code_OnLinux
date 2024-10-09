
package gc.cms;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.ListIterator;

class MyList extends LinkedList {

    int[] a;

    MyList(int size) {
        a = new int[size];
    }
}

class MyRefList extends LinkedList {

    WeakReference ref;

    MyRefList(Object o, ReferenceQueue rq) {
        ref = new WeakReference(o, rq);
    }

    void clearReferent() {
        ref.clear();
    }
}

public class TestBubbleUpRef {

    MyList list;

    MyRefList refList;

    ReferenceQueue rq;

    int refListLen;

    int arraySize;

    int iterations;

    int workUnits;

    TestBubbleUpRef(int as, int cnt, int wk) {
        arraySize = as;
        iterations = cnt;
        workUnits = wk;
        list = new MyList(arraySize);
        refList = new MyRefList(list, rq);
    }

    public void fill() {
        System.out.println("fill() " + iterations + " times");
        int count = 0;
        while (true) {
            try {
                MyList next = new MyList(arraySize);
                list.add(next);
                MyRefList nextRef = new MyRefList(next, rq);
                refList.add(nextRef);
            } catch (OutOfMemoryError e) {
                try {
                    if (count++ > iterations) {
                        return;
                    }
                    System.out.println("Freeing list");
                    while (!list.isEmpty()) {
                        list.removeFirst();
                    }
                    System.out.println("Doing work");
                    int j = 0;
                    for (int i = 1; i < workUnits; i++) {
                        j = j + i;
                    }
                    System.out.println("Clearing refs");
                    ListIterator listIt = refList.listIterator();
                    while (listIt.hasNext()) {
                        MyRefList next = (MyRefList) listIt.next();
                        next.clearReferent();
                    }
                    System.gc();
                    System.out.println("Freeing refs");
                    while (!refList.isEmpty()) {
                        refList.removeFirst();
                    }
                } catch (OutOfMemoryError e2) {
                    System.err.println("Out of Memory - 2 ");
                    continue;
                }
            } catch (Exception e) {
                System.err.println("Unexpected exception: " + e);
                return;
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Wrong number of input argumets");
        }
        int as = Integer.parseInt(args[0]);
        int cnt = Integer.parseInt(args[1]);
        int work = Integer.parseInt(args[2]);
        System.out.println("<array size> " + as + "\n" + "<OOM's> " + cnt + "\n" + "<work units> " + work + "\n");
        TestBubbleUpRef b = new TestBubbleUpRef(as, cnt, work);
        try {
            b.fill();
        } catch (OutOfMemoryError e) {
            b = null;
            System.err.println("Out of Memory - exiting ");
        } catch (Exception e) {
            System.err.println("Exiting ");
        }
    }
}
