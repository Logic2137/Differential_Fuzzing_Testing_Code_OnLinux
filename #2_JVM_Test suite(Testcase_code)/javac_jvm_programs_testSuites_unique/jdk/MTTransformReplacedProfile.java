

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public final class MTTransformReplacedProfile {

    private static volatile long endtime;

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
                    if (d1 == null || d2 == null) {
                        continue;
                    }
                    tasks.add(new Thread(() -> {
                        try {
                            test(profile1.getData(), d1, d2, tag);
                        } catch (Throwable ignored) {
                            
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
    }

    private static void test(byte[] all, byte[] data1, byte[] data2, int tag)
            throws Exception {
        ICC_Profile icc = ICC_Profile.getInstance(all);
        ColorSpace cs = new ICC_ColorSpace(icc);
        AtomicBoolean stop = new AtomicBoolean();
        Thread swap = new Thread(() -> {
            try {
                while (!isComplete()) {
                    icc.setData(tag, data1);
                    icc.setData(tag, data2);
                }
            } catch (Throwable ignored) {
                
            }
            stop.set(true);
        });

        float[] colorvalue = new float[3];
        Thread transform = new Thread(() -> {
            boolean rgb = true;
            while (!stop.get() && !isComplete()) {
                try {
                    if (rgb) {
                        cs.toRGB(colorvalue);
                    } else {
                        cs.toCIEXYZ(colorvalue);
                    }
                } catch (Throwable ignored) {
                    
                }
                rgb = !rgb;
            }
        });

        swap.start();
        transform.start();
        swap.join();
        transform.join();
    }

    private static boolean isComplete() {
        return endtime - System.nanoTime() < 0;
    }
}
