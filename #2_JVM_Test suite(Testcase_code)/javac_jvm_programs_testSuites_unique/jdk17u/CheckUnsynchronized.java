import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class CheckUnsynchronized {

    public static void main(String[] args) {
        Properties props = new Properties();
        synchronized (props) {
            props.setProperty("key", "value");
            System.out.println("contains(value)? " + CompletableFuture.supplyAsync(() -> props.contains("value")).join());
            System.out.println("containsKey(key)? " + CompletableFuture.supplyAsync(() -> props.containsKey("key")).join());
            System.out.println("containsValue(value)? " + CompletableFuture.supplyAsync(() -> props.containsValue("value")).join());
            Enumeration<Object> elems = CompletableFuture.supplyAsync(() -> props.elements()).join();
            System.out.println("first value from elements(): " + elems.nextElement());
            System.out.println("value from get(): " + CompletableFuture.supplyAsync(() -> props.getProperty("key")).join());
            System.out.println("getOrDefault(\"missing\"): " + CompletableFuture.supplyAsync(() -> props.getOrDefault("missing", "default")).join());
            System.out.println("isEmpty()? " + CompletableFuture.supplyAsync(() -> props.isEmpty()).join());
            Enumeration<Object> keys = CompletableFuture.supplyAsync(() -> props.keys()).join();
            System.out.println("first key from keys(): " + keys.nextElement());
            System.out.println("size(): " + CompletableFuture.supplyAsync(() -> props.size()).join());
        }
    }
}
