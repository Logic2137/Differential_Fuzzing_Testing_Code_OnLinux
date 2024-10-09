public class Test6935022 {

    public static final void main(String[] args) throws Exception {
        Test6935022 test = new Test6935022();
        int cnt = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (cnt < 10000) {
            try {
                ++cnt;
                if ((cnt & 1023) == 0)
                    System.out.println("Thread=" + Thread.currentThread().getName() + " iteration: " + cnt);
                test.loop(2147483647, (cnt & 1023));
            } catch (Exception e) {
                System.out.println("Caught on iteration " + cnt);
                e.printStackTrace();
                System.exit(97);
            }
        }
    }

    private void loop(int endingRow, int mask) throws Exception {
        int rows = 1;
        boolean next = true;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (rows <= endingRow && next) {
            rows++;
            if (rows == mask)
                System.out.println("Rows=" + rows + ", end=" + endingRow + ", next=" + next);
            next = next(rows);
        }
        if (next)
            throw new Exception("Ended on rows(no rs): " + rows);
    }

    private boolean next(int rows) {
        return rows < 12;
    }
}
