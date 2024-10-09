

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class StreamHandler implements Runnable {

    public interface Listener {
        
        void onStringRead(StreamHandler handler, String s);
    }

    private final ExecutorService executor;
    private final InputStream is;
    private final Listener listener;

    
    public StreamHandler(InputStream is, Listener listener) throws IOException {
        this.is = is;
        this.listener = listener;
        executor = Executors.newSingleThreadExecutor();
    }

    
    public void start() {
        executor.submit(this);
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                listener.onStringRead(this, line);
            }
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

}
