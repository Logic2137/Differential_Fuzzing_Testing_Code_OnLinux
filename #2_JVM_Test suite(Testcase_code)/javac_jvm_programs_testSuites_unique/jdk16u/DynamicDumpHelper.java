

package cdsutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class DynamicDumpHelper {
    public static void main(String args[]) throws Throwable {
        File file = new File(args[0]);

        System.out.println("Loading classes to share...");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                
                line = line.replace('/', '.');
                try {
                    Class.forName(line);
                } catch (java.lang.ClassNotFoundException ex) {
                    try {
                        Class.forName(line, true, null);
                    } catch (java.lang.ClassNotFoundException cnfe) {
                        System.out.println("Preload Warning: Cannot find " + line.replace('.', '/'));
                    }
                } catch (Throwable t) {
                    System.out.println("Error: failed to load \"" + line + "\": " + t);
                }
            }
        }
        System.out.println("Loading classes to share: done.");
    }
}
