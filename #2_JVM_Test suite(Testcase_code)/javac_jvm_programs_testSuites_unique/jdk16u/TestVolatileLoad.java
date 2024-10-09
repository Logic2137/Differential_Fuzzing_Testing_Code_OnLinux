

package compiler.c2.aarch64;

class TestVolatileLoad
{
    public volatile int f_int = 0;
    public volatile Integer f_obj = Integer.valueOf(0);

    public static void main(String[] args)
    {
        final TestVolatileLoad t = new TestVolatileLoad();
        for (int i = 0; i < 100_000; i++) {
            t.f_int = i;
            int r = t.testInt();
            if (r != i) {
                throw new RuntimeException("bad result!");
            }
        }
        for (int i = 0; i < 100_000; i++) {
            t.f_obj = Integer.valueOf(i);
            int r = t.testObj();
            if (r != i) {
                throw new RuntimeException("bad result!");
            }
        }
    }
    public int testInt()
    {
        return f_int;
    }

    public int testObj()
    {
        return f_obj;
    }
}
