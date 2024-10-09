

import java.nio.file.*;
import java.io.IOException;
import java.util.*;



public class CreateFileTree {

    private static final Random rand = new Random();

    private static boolean supportsLinks(Path dir) {
        Path link = dir.resolve("testlink");
        Path target = dir.resolve("testtarget");
        try {
            Files.createSymbolicLink(link, target);
            Files.delete(link);
            return true;
        } catch (UnsupportedOperationException x) {
            return false;
        } catch (IOException x) {
            return false;
        }
    }

    static Path create() throws IOException {
        Path top = Files.createTempDirectory("tree");
        List<Path> dirs = new ArrayList<Path>();

        
        Queue<Path> queue = new ArrayDeque<Path>();
        queue.add(top);
        int total = 1 + rand.nextInt(20);
        int n = 0;
        Path dir;
        while (((dir = queue.poll()) != null) && (n < total)) {
            int r = Math.min((total-n), (1+rand.nextInt(3)));
            for (int i=0; i<r; i++) {
                String name = "dir" + (++n);
                Path subdir = Files.createDirectory(dir.resolve(name));
                queue.offer(subdir);
                dirs.add(subdir);
            }
        }

        
        int files = dirs.size() * 3;
        for (int i=0; i<files; i++) {
            String name = "file" + (i+1);
            int x = rand.nextInt(dirs.size());
            Files.createFile(dirs.get(x).resolve(name));
        }

        
        if (supportsLinks(top)) {
            int links = 1 + rand.nextInt(5);
            for (int i=0; i<links; i++) {
                int x = rand.nextInt(dirs.size());
                int y;
                do {
                    y = rand.nextInt(dirs.size());
                } while (y != x);
                String name = "link" + (i+1);
                Path link = dirs.get(x).resolve(name);
                Path target = dirs.get(y);
                Files.createSymbolicLink(link, target);
            }
        }

         return top;
    }

    public static void main(String[] args) throws IOException {
        Path top = create();
        System.out.println(top);
    }
}
