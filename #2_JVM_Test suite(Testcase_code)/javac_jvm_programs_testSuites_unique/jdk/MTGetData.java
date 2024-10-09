

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public final class MTGetData {

    static volatile long endtime;
    static volatile boolean failed;

    public static void main(String[] args) throws Exception {
        ICC_Profile[] profiles = {
                ICC_Profile.getInstance(ColorSpace.CS_sRGB),
                ICC_Profile.getInstance(ColorSpace.CS_LINEAR_RGB),
                ICC_Profile.getInstance(ColorSpace.CS_CIEXYZ),
                ICC_Profile.getInstance(ColorSpace.CS_PYCC),
                ICC_Profile.getInstance(ColorSpace.CS_GRAY)
        };

        List<Integer> tags = new ArrayList<>();
        for (Field field : ICC_Profile.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())
                    && Modifier.isPublic(field.getModifiers())
                    && Modifier.isFinal(field.getModifiers())
                    && field.getType() == int.class) {
                tags.add(field.getInt(null));
            }
        }

        List<Thread> tasks = new ArrayList<>();
        for (int tag : tags) {
            for (ICC_Profile profile1 : profiles) {
                for (ICC_Profile profile2 : profiles) {
                    byte[] d1 = profile1.getData(tag);
                    byte[] d2 = profile2.getData(tag);
                    if (d1 == null || d2 == null || d1.length == d2.length) {
                        continue;
                    }
                    tasks.add(new Thread(() -> {
                        try {
                            test(profile1.getData(), d1, d2, tag);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                            failed = true;
                        }
                    }));
                }
            }
        }

        
        endtime = System.nanoTime() + TimeUnit.SECONDS.toNanos(15);
        for (Thread t : tasks) {
            t.start();
        }
        for (Thread t : tasks) {
            t.join();
        }
        if (failed) {
            throw new RuntimeException();
        }
    }

    private static void test(byte[] all, byte[] data1, byte[] data2, int tag)
            throws Exception {
        ICC_Profile icc = ICC_Profile.getInstance(all);

        Thread swap = new Thread(() -> {
            try {
                while (!isComplete()) {
                    icc.setData(tag, data1);
                    icc.setData(tag, data2);
                }
            } catch (IllegalArgumentException ignored) {
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                failed = true;
            }
        });

        Thread fetch = new Thread(() -> {
            try {
                while (!isComplete()) {
                    icc.getData(tag);
                    icc.getData();
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                failed = true;
            }
        });

        swap.start();
        fetch.start();
        swap.join();
        fetch.join();
    }

    private static boolean isComplete() {
        return endtime - System.nanoTime() < 0 || failed;
    }
}
