
package gc.hashcode.ExternalHashingTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

public final class ExternalHashingTest {

    static Random rand = new Random();

    public static volatile boolean startingGun;

    public static volatile boolean finishHashing;

    public static volatile boolean finishLocking;

    private static final int BATCH_SIZE = 20;

    static Vector allObjects = new Vector();

    static Vector allHashes = new Vector();

    private static final long DEFAULT_DURATION = 10000;

    private ExternalHashingTest() {
    }

    public static Object[] garbageMonger;

    public static void pause() {
        try {
            Thread.yield();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss z");
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        return df.format(date);
    }

    public static void main(String[] args) {
        long timeToRun = DEFAULT_DURATION;
        ;
        try {
            for (int i = 0; i < args.length; i++) {
                if ("-stressTime".equals(args[i])) {
                    if (i + 1 == args.length) {
                        throw new RuntimeException("Test bug: value of -stressTime option absents");
                    }
                    timeToRun = Long.parseLong(args[i + 1]);
                    if (timeToRun <= 0) {
                        throw new RuntimeException("Test bug: value of -stressTime option is not a positive number");
                    }
                    break;
                }
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Test bug: Exception occured while parsing -stressTime option's value", e);
        }
        long startTime = System.currentTimeMillis();
        System.out.println("[" + getDateString() + "] Test duration is: " + timeToRun + " ms");
        System.out.println("[" + getDateString() + "] Do munge objects...");
        while ((System.currentTimeMillis() - startTime) < timeToRun) {
            for (int i = 0; i < 100; i++) {
                mungeObjects();
            }
            System.out.println("[" + getDateString() + "] The next 100 objects are munged...");
        }
        System.out.println("[" + getDateString() + "] Force a GC...");
        garbageMonger = null;
        System.gc();
        System.out.println("[" + getDateString() + "] Check hash codes...");
        for (int i = 0; i < allObjects.size(); i++) {
            Object o = allObjects.elementAt(i);
            int hash = ((Integer) allHashes.elementAt(i)).intValue();
            if (o.hashCode() != hash) {
                System.out.println("Inconsistent hash code found (Object " + i + " out of " + allObjects.size());
                System.out.println("Object = " + o.toString() + "; hashCode = 0x" + Integer.toHexString(o.hashCode()) + "; expected = 0x" + Integer.toHexString(hash));
                System.exit(1);
            }
        }
        System.exit(95);
    }

    private static void mungeObjects() {
        startingGun = false;
        finishHashing = false;
        finishLocking = false;
        Object[] candidates = new Object[BATCH_SIZE];
        for (int i = 0; i < candidates.length; i++) {
            candidates[i] = new Object();
        }
        Object[] lockedList = randomize(candidates);
        Object[] hashedList = randomize(candidates);
        int[] foundHashes = new int[BATCH_SIZE];
        LockerThread locker = new LockerThread(lockedList);
        Thread lockerThread = new Thread(locker);
        Thread hasherThread = new Thread(new HasherThread(hashedList, foundHashes));
        lockerThread.start();
        hasherThread.start();
        startingGun = true;
        while (!finishLocking || !finishHashing) {
            pause();
        }
        garbageMonger = new Object[BATCH_SIZE];
        for (int i = 0; i < BATCH_SIZE; i++) {
            allObjects.add(hashedList[i]);
            allHashes.add(new Integer(foundHashes[i]));
            garbageMonger[i] = new Object();
        }
        if (locker.getCount() != BATCH_SIZE) {
            throw new InternalError("should not get here");
        }
    }

    private static Object[] randomize(Object[] list) {
        Vector v = new Vector();
        for (int i = 0; i < list.length; i++) {
            v.add(list[i]);
        }
        Object[] result = new Object[list.length];
        for (int i = 0; i < list.length; i++) {
            int pos = rand.nextInt(list.length - i);
            result[i] = v.remove(pos);
        }
        return result;
    }
}

class LockerThread implements Runnable {

    Object[] theList;

    int count;

    LockerThread(Object[] list) {
        theList = list;
        count = 0;
    }

    public void run() {
        while (!ExternalHashingTest.startingGun) {
            ExternalHashingTest.pause();
        }
        for (int i = 0; i < theList.length; i++) {
            synchronized (theList[i]) {
                count++;
            }
        }
        ExternalHashingTest.finishLocking = true;
    }

    public int getCount() {
        return count;
    }
}

class HasherThread implements Runnable {

    Object[] theList;

    int[] theHashes;

    HasherThread(Object[] list, int[] hashes) {
        theList = list;
        theHashes = hashes;
    }

    public void run() {
        while (!ExternalHashingTest.startingGun) {
            ExternalHashingTest.pause();
        }
        for (int i = 0; i < theList.length; i++) {
            theHashes[i] = theList[i].hashCode();
        }
        ExternalHashingTest.finishHashing = true;
    }
}
