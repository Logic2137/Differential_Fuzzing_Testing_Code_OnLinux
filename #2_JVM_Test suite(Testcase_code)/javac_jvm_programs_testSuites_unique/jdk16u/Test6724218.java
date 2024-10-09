



package compiler.c2;

public class Test6724218 {
    Test6724218 next  = null;
    Object value = null;

    static boolean _closed = false;
    static int size = 0;
    static Test6724218 list  = null;
    static int cache_size = 0;
    static Test6724218 cache = null;

    Object get(int i) {
        Test6724218 t = list;
        list = t.next;
        size -= 1;
        Object o = t.value;
        if (i > 0) {
            t.next = cache;
            t.value = null;
            cache = t;
            cache_size = +1;
        }
        return o;
    }

    void update() {
        
        if (size == 0) {
            Test6724218 t;
            if (cache_size > 0) {
                t = cache;
                cache = t.next;
                cache_size = -1;
            } else {
                t = new Test6724218();
            }
            t.value = new Object();
            t.next = list;
            list = t;
            size += 1;
        }
    }

    synchronized Object test(int i) {
        while (true) {
            if (_closed) {
                return null;
            } else if (size > 0) {
                return get(i);
            }
            update();
        }
    }

    public static void main(String argv[]) throws Exception {
        Test6724218 t = new Test6724218();
        int lim = 500000;
        Object o;
        for (int j = 0; j < lim; j++) {
            o = t.test(j&1);
            if (o == null) {
              throw new Exception("*** Failed on iteration " + j);
            }
            if ((j&1) == 0) {
              t.update();
            }
        }
    }
}
