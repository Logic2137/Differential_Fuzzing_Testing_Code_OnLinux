




class LoopHelpers {

    

    
    public static int compute1(int x) {
        int lo = 16807 * (x & 0xFFFF);
        int hi = 16807 * (x >>> 16);
        lo += (hi & 0x7FFF) << 16;
        if ((lo & 0x80000000) != 0) {
            lo &= 0x7fffffff;
            ++lo;
        }
        lo += hi >>> 15;
        if (lo == 0 || (lo & 0x80000000) != 0) {
            lo &= 0x7fffffff;
            ++lo;
        }
        return lo;
    }

    
    public static int compute2(int x) {
        int loops = (x >>> 4) & 7;
        while (loops-- > 0) {
            x = (x * 2147483647) % 16807;
        }
        return x;
    }

    public static class BarrierTimer implements Runnable {
        public volatile long startTime;
        public volatile long endTime;
        public void run() {
            long t = System.nanoTime();
            if (startTime == 0)
                startTime = t;
            else
                endTime = t;
        }
        public void clear() {
            startTime = 0;
            endTime = 0;
        }
        public long getTime() {
            return endTime - startTime;
        }
    }

    public static String rightJustify(long n) {
        
        String field = "         ";
        String num = Long.toString(n);
        if (num.length() >= field.length())
            return num;
        StringBuilder b = new StringBuilder(field);
        b.replace(b.length()-num.length(), b.length(), num);
        return b.toString();
    }

}
