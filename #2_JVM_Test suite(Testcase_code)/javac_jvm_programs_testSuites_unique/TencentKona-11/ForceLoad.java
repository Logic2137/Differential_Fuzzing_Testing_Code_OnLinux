



import java.nio.file.*;
import java.io.IOException;

public class ForceLoad {

    public static void main(String[] args) throws IOException {
        Files.probeContentType(Paths.get("."));
    }
}
