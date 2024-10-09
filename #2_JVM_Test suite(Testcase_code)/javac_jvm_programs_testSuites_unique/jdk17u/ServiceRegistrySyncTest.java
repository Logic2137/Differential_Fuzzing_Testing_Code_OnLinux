import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.stream.ImageInputStream;

public class ServiceRegistrySyncTest {

    public static void main(String[] args) throws InterruptedException {
        final ArrayList<Class<?>> services = new ArrayList<Class<?>>();
        services.add(ImageInputStreamSpi.class);
        final ServiceRegistry reg = new ServiceRegistry(services.iterator());
        Thread registerer = new Thread(new Registerer(reg));
        Thread consumer = new Thread(new Consumer(reg));
        registerer.start();
        consumer.start();
    }

    static class Consumer implements Runnable {

        private final ServiceRegistry reg;

        boolean go = true;

        int duration;

        long start, end;

        public Consumer(ServiceRegistry r) {
            reg = r;
            duration = 5000;
        }

        @Override
        public void run() {
            start = System.currentTimeMillis();
            end = start + duration;
            while (start < end) {
                reg.getServiceProviders(ImageInputStreamSpi.class, true);
                start = System.currentTimeMillis();
            }
        }
    }

    static class Registerer implements Runnable {

        private final ServiceRegistry reg;

        boolean go = true;

        int duration;

        long start, end;

        public Registerer(ServiceRegistry r) {
            reg = r;
            duration = 5000;
        }

        @Override
        public void run() {
            final int N = 20;
            MyService[] services = new MyService[N];
            for (int i = 0; i < N; i++) {
                services[i] = new MyService();
            }
            start = System.currentTimeMillis();
            end = start + duration;
            while (start < end) {
                for (int i = 0; i < N; i++) {
                    reg.registerServiceProvider(services[i]);
                }
                for (int i = 0; i < N; i++) {
                    reg.deregisterServiceProvider(services[i]);
                }
                start = System.currentTimeMillis();
            }
        }
    }
}

class MyService extends ImageInputStreamSpi {

    public MyService() {
    }

    @Override
    public String getDescription(Locale locale) {
        return null;
    }

    @Override
    public ImageInputStream createInputStreamInstance(Object input, boolean useCache, File cacheDir) throws IOException {
        return null;
    }
}
