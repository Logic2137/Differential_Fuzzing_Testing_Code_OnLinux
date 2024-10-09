




import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NegativeSOURCETest {

    NegativeSOURCETest(String dataFile) {
        
        readFile(dataFile);
    }

    private void readFile(String fileName) {
        String fishForSOURCE = null;

        File file = new File(fileName);

        
        try (BufferedReader buffRead =
            new BufferedReader(new FileReader(fileName))) {

            
            String readIn;

            
            while ((readIn = buffRead.readLine()) != null) {
                readIn = readIn.trim();

                
                if (readIn.length() == 0)
                    continue;

                
                if (readIn.startsWith("SOURCE=")) {
                    fishForSOURCE = readIn;
                    break;
                }
            }
        } catch (FileNotFoundException fileExcept) {
            throw new RuntimeException("File " + fileName +
                                       " not found reading data!", fileExcept);
        } catch (IOException ioExcept) {
            throw new RuntimeException("Unexpected problem reading data!",
                                       ioExcept);
        }

        
        if (fishForSOURCE == null) {
            throw new RuntimeException("SOURCE line was not found!");
        } else {
            
            if (fishForSOURCE.contains("closed") || fishForSOURCE.contains("open")) {
                System.out.println("The offending string: " + fishForSOURCE);
                throw new RuntimeException("The test failed, closed/open found!");
            }
        }

        
        System.out.println("The test passed!");
    }

    public static void main(String args[]) {
        String jdkPath = System.getProperty("test.jdk");
        String runtime = System.getProperty("java.runtime.name");

        System.out.println("JDK Path : " + jdkPath);
        System.out.println("Runtime Name : " + runtime);

        if (runtime.contains("OpenJDK"))
            new NegativeSOURCETest(jdkPath + "/release");
        else
            System.out.println("Test skipped: not an OpenJDK build.");
    }
}
