import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.toList;

public class RangeCheckMicroBenchmark {

    abstract static class Job {

        private final String name;

        Job(String name) {
            this.name = name;
        }

        String name() {
            return name;
        }

        abstract void work() throws Throwable;
    }

    private static void collectAllGarbage() {
        final CountDownLatch drained = new CountDownLatch(1);
        try {
            System.gc();
            new Object() {

                protected void finalize() {
                    drained.countDown();
                }
            };
            System.gc();
            drained.await();
            System.gc();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    private static long[] time0(Job... jobs) throws Throwable {
        final long warmupNanos = 10L * 1000L * 1000L * 1000L;
        long[] nanoss = new long[jobs.length];
        for (int i = 0; i < jobs.length; i++) {
            collectAllGarbage();
            long t0 = System.nanoTime();
            long t;
            int j = 0;
            do {
                jobs[i].work();
                j++;
            } while ((t = System.nanoTime() - t0) < warmupNanos);
            nanoss[i] = t / j;
        }
        return nanoss;
    }

    private static void time(Job... jobs) throws Throwable {
        long[] warmup = time0(jobs);
        long[] nanoss = time0(jobs);
        long[] milliss = new long[jobs.length];
        double[] ratios = new double[jobs.length];
        final String nameHeader = "Method";
        final String millisHeader = "Millis";
        final String ratioHeader = "Ratio";
        int nameWidth = nameHeader.length();
        int millisWidth = millisHeader.length();
        int ratioWidth = ratioHeader.length();
        for (int i = 0; i < jobs.length; i++) {
            nameWidth = Math.max(nameWidth, jobs[i].name().length());
            milliss[i] = nanoss[i] / (1000L * 1000L);
            millisWidth = Math.max(millisWidth, String.format("%d", milliss[i]).length());
            ratios[i] = (double) nanoss[i] / (double) nanoss[0];
            ratioWidth = Math.max(ratioWidth, String.format("%.3f", ratios[i]).length());
        }
        String format = String.format("%%-%ds %%%dd %%%d.3f%%n", nameWidth, millisWidth, ratioWidth);
        String headerFormat = String.format("%%-%ds %%%ds %%%ds%%n", nameWidth, millisWidth, ratioWidth);
        System.out.printf(headerFormat, "Method", "Millis", "Ratio");
        for (int i = 0; i < jobs.length; i++) System.out.printf(format, jobs[i].name(), milliss[i], ratios[i]);
    }

    private static String keywordValue(String[] args, String keyword) {
        for (String arg : args) if (arg.startsWith(keyword))
            return arg.substring(keyword.length() + 1);
        return null;
    }

    private static int intArg(String[] args, String keyword, int defaultValue) {
        String val = keywordValue(args, keyword);
        return val == null ? defaultValue : Integer.parseInt(val);
    }

    private static Pattern patternArg(String[] args, String keyword) {
        String val = keywordValue(args, keyword);
        return val == null ? null : Pattern.compile(val);
    }

    private static Job[] filter(Pattern filter, Job[] jobs) {
        return (filter == null) ? jobs : Arrays.stream(jobs).filter(job -> filter.matcher(job.name()).find()).collect(toList()).toArray(new Job[0]);
    }

    private static void deoptimize(ArrayList<Integer> list) {
        for (Integer x : list) if (x == null)
            throw new Error();
    }

    public static void main(String[] args) throws Throwable {
        final int iterations = intArg(args, "iterations", 30000);
        final int size = intArg(args, "size", 1000);
        final Pattern filter = patternArg(args, "filter");
        final ArrayList<Integer> list = new ArrayList<>();
        final Random rnd = new Random();
        for (int i = 0; i < size; i++) list.add(rnd.nextInt());
        final Job[] jobs = { new Job("get") {

            void work() {
                for (int i = 0; i < iterations; i++) {
                    for (int k = 0; k < size; k++) if (list.get(k) == 42)
                        throw new Error();
                }
                deoptimize(list);
            }
        }, new Job("set") {

            void work() {
                Integer[] xs = list.toArray(new Integer[size]);
                for (int i = 0; i < iterations; i++) {
                    for (int k = 0; k < size; k++) list.set(k, xs[k]);
                }
                deoptimize(list);
            }
        }, new Job("get/set") {

            void work() {
                for (int i = 0; i < iterations; i++) {
                    for (int k = 0; k < size; k++) list.set(k, list.get(size - k - 1));
                }
                deoptimize(list);
            }
        }, new Job("add/remove at end") {

            void work() {
                Integer x = rnd.nextInt();
                for (int i = 0; i < iterations; i++) {
                    for (int k = 0; k < size - 1; k++) {
                        list.add(size, x);
                        list.remove(size);
                    }
                }
                deoptimize(list);
            }
        }, new Job("subList get") {

            void work() {
                List<Integer> sublist = list.subList(0, list.size());
                for (int i = 0; i < iterations; i++) {
                    for (int k = 0; k < size; k++) if (sublist.get(k) == 42)
                        throw new Error();
                }
                deoptimize(list);
            }
        }, new Job("subList set") {

            void work() {
                List<Integer> sublist = list.subList(0, list.size());
                Integer[] xs = sublist.toArray(new Integer[size]);
                for (int i = 0; i < iterations; i++) {
                    for (int k = 0; k < size; k++) sublist.set(k, xs[k]);
                }
                deoptimize(list);
            }
        }, new Job("subList get/set") {

            void work() {
                List<Integer> sublist = list.subList(0, list.size());
                for (int i = 0; i < iterations; i++) {
                    for (int k = 0; k < size; k++) sublist.set(k, sublist.get(size - k - 1));
                }
                deoptimize(list);
            }
        }, new Job("subList add/remove at end") {

            void work() {
                List<Integer> sublist = list.subList(0, list.size());
                Integer x = rnd.nextInt();
                for (int i = 0; i < iterations; i++) {
                    for (int k = 0; k < size - 1; k++) {
                        sublist.add(size, x);
                        sublist.remove(size);
                    }
                }
                deoptimize(list);
            }
        } };
        time(filter(filter, jobs));
    }
}
