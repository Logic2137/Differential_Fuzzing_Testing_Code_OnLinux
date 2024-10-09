

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Bug8066652 {

    public static void main(String args[]) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Callable<TimeZone> calTimeZone = () -> TimeZone.getDefault();
        List<Callable<TimeZone>> tasks = new ArrayList<>();
        for (int j = 1; j < 10; j++) {
            tasks.add(calTimeZone);
        }
        try {
            List<Future<TimeZone>> results = executor.invokeAll(tasks);
            for (Future<TimeZone> f : results) {
                TimeZone tz = f.get();
                if (! tz.getID().equals("GMT")) {
                    throw new RuntimeException("wrong Time zone ID: " + tz.getID()
                            + ", It should be GMT");
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Execution interrupted or Execution Exception occurred", e);
        } finally {
            executor.shutdown();
        }
    }
}
