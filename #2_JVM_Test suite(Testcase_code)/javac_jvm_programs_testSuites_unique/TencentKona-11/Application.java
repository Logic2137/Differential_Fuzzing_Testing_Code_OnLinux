


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Application {
    public static final String READY_MSG="ready";
    public static final String SHUTDOWN_MSG="shutdown";

    public static void main(String args[]) throws Exception {
        System.out.println(READY_MSG);
        System.out.flush();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (!br.readLine().equals(SHUTDOWN_MSG));
        }
    }
}
