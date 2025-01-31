

package nsk.stress.jni;

class GarbageGenerator extends Thread {
    class Garbage {
        Garbage() {
            this(1024);
        }

        Garbage(int m) {
            memory = new byte[m];
        }

        void setNext(Garbage n) {
            next = n;
        }

        Garbage getNext() {
            return next;
        }

        protected void finalize() {
        }

        private Garbage next;
        private byte[] memory;
    }

    class GarbageRing {
        GarbageRing() {
            attachment = new Garbage(0);
        }

        void add(int size) {
            Garbage head = attachment.getNext();
            Garbage g = new Garbage(size);
            if (head != null) {
                Garbage oldNext = head.getNext();
                if (oldNext != null) {
                    g.setNext(oldNext);
                    head.setNext(g);
                    attachment.setNext(g);
                } else {
                    g.setNext(head);
                    head.setNext(g);
                }
            } else
                attachment.setNext(g);
        }

        void discard() {
            attachment.setNext(null);
        }

        private byte[] memory;
        private Garbage attachment;
    }

    public void run() {
        GarbageRing gr = new GarbageRing();
        int g = 0;
        while (!done) {
            for (g = 0; g < ringSize; g++) {
                gr.add(allocSize);
                yield();
            }
            gr.discard();
            try {
                sleep(interval);
            } catch (InterruptedException e) {
            }
        }
        if (DEBUG) System.out.println("GarbageRing::run(): done");
    }

    public void setAllocSize(int i) {
        allocSize = i;
    }

    public int getAllocSize() {
        return allocSize;
    }

    public void setInterval(int i) {
        interval = i;
    }

    public int getInterval() {
        return interval;
    }

    public void halt() {
        done = true;
    }

    private int allocSize = 10000;
    private int ringSize = 50;
    private int interval = 1000;
    private boolean done = false;
    final private static boolean DEBUG = false;
}
