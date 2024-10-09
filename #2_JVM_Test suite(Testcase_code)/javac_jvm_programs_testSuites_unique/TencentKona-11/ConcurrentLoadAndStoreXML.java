



import java.io.*;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ConcurrentLoadAndStoreXML {

    static final Random RAND = new Random();

    static volatile boolean done;

    
    static class Basher implements Callable<Void> {
        final Properties props;

        Basher(Properties props) {
            this.props = props;
        }

        public Void call() throws IOException {
            while (!done) {

                
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                props.storeToXML(out, null, "UTF-8");

                
                Properties p = new Properties();
                ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                p.loadFromXML(in);

                
                if (!p.equals(props))
                    throw new RuntimeException("Properties not equal");
            }
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        final int NTASKS = 4 + RAND.nextInt(4);

        
        Basher[] basher = new Basher[NTASKS];
        for (int i=0; i<NTASKS; i++) {
            Properties props = new Properties();
            for (int j=0; j<RAND.nextInt(100); j++) {
                String key = "k" + RAND.nextInt(1000);
                String value = "v" + RAND.nextInt(1000);
                props.put(key, value);
            }
            basher[i] = new Basher(props);
        }

        ExecutorService pool = Executors.newFixedThreadPool(NTASKS);
        try {
            
            Future<Void>[] task = new Future[NTASKS];
            for (int i=0; i<NTASKS; i++) {
                task[i] = pool.submit(basher[i]);
            }

            
            Thread.sleep(2000);
            done = true;

            
            for (int i=0; i<NTASKS; i++) {
                task[i].get();
            }
        } finally {
            done = true;
            pool.shutdown();
        }

    }
}
