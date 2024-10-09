

package compiler.c2.aarch64;

class TestVolatileStore
{
    public volatile int f_int = 0;
    public volatile Integer f_obj = Integer.valueOf(0);

    public static void main(String[] args)
    {
        final TestVolatileStore t = new TestVolatileStore();
        for (int i = 0; i < 100_000; i++) {
            t.f_int = -1;
            t.testInt(i);
            if (t.f_int != i) {
                throw new RuntimeException("bad result!");
            }
        }
        for (int i = 0; i < 100_000; i++) {
            t.f_obj = null;
            t.testObj(Integer.valueOf(i));
            if (t.f_obj != i) {
                throw new RuntimeException("bad result!");
            }
        }
    }
    public void testInt(int i)
    {
        f_int = i;
    }

    public void testObj(Integer o)
    {
        f_obj = o;
    }
}
