import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class MTICC_ColorSpaceToFrom {

    private enum Method {

        FROM_RGB, FROM_XYZ, TO_RGB, TO_XYZ
    }

    static volatile long endtime;

    static volatile boolean failed;

    public static void main(String[] args) throws Exception {
        ICC_Profile srgb = ICC_Profile.getInstance(ColorSpace.CS_sRGB);
        ICC_Profile gray = ICC_Profile.getInstance(ColorSpace.CS_GRAY);
        ICC_Profile xyz = ICC_Profile.getInstance(ColorSpace.CS_CIEXYZ);
        ICC_Profile lrgb = ICC_Profile.getInstance(ColorSpace.CS_LINEAR_RGB);
        ICC_Profile pycc = ICC_Profile.getInstance(ColorSpace.CS_PYCC);
        endtime = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
        for (int i = 0; i < 1000 && !isComplete(); i++) {
            for (Method method : new Method[] { Method.FROM_RGB, Method.FROM_XYZ, Method.TO_RGB, Method.TO_XYZ }) {
                test(new ICC_ColorSpace(srgb), method);
                test(new ICC_ColorSpace(gray), method);
                test(new ICC_ColorSpace(xyz), method);
                test(new ICC_ColorSpace(lrgb), method);
                test(new ICC_ColorSpace(pycc), method);
            }
        }
        if (failed) {
            throw new RuntimeException();
        }
    }

    private static void test(ColorSpace cs, Method method) throws Exception {
        Thread[] ts = new Thread[10];
        CountDownLatch latch = new CountDownLatch(ts.length);
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(() -> {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException ex) {
                }
                try {
                    switch(method) {
                        case TO_RGB:
                            cs.toRGB(new float[3]);
                        case FROM_RGB:
                            cs.fromRGB(new float[3]);
                        case TO_XYZ:
                            cs.toCIEXYZ(new float[3]);
                        case FROM_XYZ:
                            cs.fromCIEXYZ(new float[3]);
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                    failed = true;
                }
            });
        }
        for (Thread t : ts) {
            t.start();
        }
        for (Thread t : ts) {
            t.join();
        }
    }

    private static boolean isComplete() {
        return endtime - System.nanoTime() < 0 || failed;
    }
}
