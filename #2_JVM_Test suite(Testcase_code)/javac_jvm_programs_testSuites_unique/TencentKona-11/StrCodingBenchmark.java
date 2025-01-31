

import java.util.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;


public class StrCodingBenchmark {
    abstract static class Job {
        private final String name;
        public Job(String name) { this.name = name; }
        public String name() { return name; }
        public abstract void work() throws Throwable;
    }

    private static void collectAllGarbage() {
        final java.util.concurrent.CountDownLatch drained
            = new java.util.concurrent.CountDownLatch(1);
        try {
            System.gc();        
            new Object() { protected void finalize() {
                drained.countDown(); }};
            System.gc();        
            drained.await();    
            System.gc();        
        } catch (InterruptedException e) { throw new Error(e); }
    }

    
    public static long[] time0(Job ... jobs) throws Throwable {
        
        final long warmupNanos = 100L * 100L;
        long[] nanoss = new long[jobs.length];
        for (int i = 0; i < jobs.length; i++) {
            collectAllGarbage();
            long t0 = System.nanoTime();
            long t;
            int j = 0;
            do { jobs[i].work(); j++; }
            while ((t = System.nanoTime() - t0) < warmupNanos);
            nanoss[i] = t/j;
        }
        return nanoss;
    }

    public static long[] time(Job ... jobs) throws Throwable {

        long[] warmup = time0(jobs); 
        long[] nanoss = time0(jobs); 
        long[] milliss = new long[jobs.length];
        double[] ratios = new double[jobs.length];

        final String nameHeader   = "Method";
        final String millisHeader = "Millis";
        final String ratioHeader  = "Ratio";

        int nameWidth   = nameHeader.length();
        int millisWidth = millisHeader.length();
        int ratioWidth  = ratioHeader.length();

        for (int i = 0; i < jobs.length; i++) {
            nameWidth = Math.max(nameWidth, jobs[i].name().length());

            milliss[i] = nanoss[i]/(1000L * 1000L);
            millisWidth = Math.max(millisWidth,
                                   String.format("%d", milliss[i]).length());

            ratios[i] = (double) nanoss[i] / (double) nanoss[0];
            ratioWidth = Math.max(ratioWidth,
                                  String.format("%.3f", ratios[i]).length());
        }
        String format = String.format("%%-%ds %%%dd %n",
                                      nameWidth, millisWidth);
        String headerFormat = String.format("%%-%ds %%%ds%n",
                                            nameWidth, millisWidth);
        System.out.printf(headerFormat, "Method", "Millis");

        
        for (int i = 0; i < jobs.length; i++)
            System.out.printf(format, jobs[i].name(), milliss[i], ratios[i]);
        return milliss;
    }

    public static Job[] filter(Pattern filter, Job[] jobs) {
        if (filter == null) return jobs;
        Job[] newJobs = new Job[jobs.length];
        int n = 0;
        for (Job job : jobs)
            if (filter.matcher(job.name()).find())
                newJobs[n++] = job;
        
        Job[] ret = new Job[n];
        System.arraycopy(newJobs, 0, ret, 0, n);
        return ret;
    }

    static class PermissiveSecurityManger extends SecurityManager {
        @Override public void checkPermission(java.security.Permission p) {
        }
    }

    public static void main(String[] args) throws Throwable {
        final int itrs = Integer.getInteger("iterations", 100000);
        final int size       = Integer.getInteger("size", 2048);
        final int subsize    = Integer.getInteger("subsize", 128);
        final int maxchar    = Integer.getInteger("maxchar", 128);
        final String regex = System.getProperty("filter");
        final Pattern filter = (regex == null) ? null : Pattern.compile(regex);
        final boolean useSecurityManager = Boolean.getBoolean("SecurityManager");
        if (useSecurityManager)
            System.setSecurityManager(new PermissiveSecurityManger());
        final Random rnd = new Random();

        for (Charset charset:  Charset.availableCharsets().values()) {
            if (!("ISO-8859-1".equals(charset.name()) ||
                  "US-ASCII".equals(charset.name()) ||
                  charset.newDecoder() instanceof sun.nio.cs.SingleByte.Decoder))
                continue;
            final String csn = charset.name();
            final Charset cs = charset;
            final StringBuilder sb = new StringBuilder();
            {
                final CharsetEncoder enc = cs.newEncoder();
                for (int i = 0; i < size; ) {
                    char c = (char) rnd.nextInt(maxchar);
                    if (enc.canEncode(c)) {
                        sb.append(c);
                        i++;
                    }
                }
            }
            final String string = sb.toString();
            final byte[] bytes  = string.getBytes(cs);

            System.out.printf("%n--------%s---------%n", csn);
            for (int sz = 4; sz <= 2048; sz *= 2) {
                System.out.printf("   [len=%d]%n", sz);
                final byte[] bs  = Arrays.copyOf(bytes, sz);
                final String str = new String(bs, csn);
                Job[] jobs = {
                    new Job("String decode: csn") {
                    public void work() throws Throwable {
                        for (int i = 0; i < itrs; i++)
                            new String(bs, csn);
                    }},

                    new Job("String decode: cs") {
                    public void work() throws Throwable {
                        for (int i = 0; i < itrs; i++)
                            new String(bs, cs);
                    }},

                    new Job("String encode: csn") {
                    public void work() throws Throwable {
                        for (int i = 0; i < itrs; i++)
                                str.getBytes(csn);
                    }},

                    new Job("String encode: cs") {
                    public void work() throws Throwable {
                         for (int i = 0; i < itrs; i++)
                          str.getBytes(cs);
                    }},
                };
                time(filter(filter, jobs));
            }
        }
    }
}
