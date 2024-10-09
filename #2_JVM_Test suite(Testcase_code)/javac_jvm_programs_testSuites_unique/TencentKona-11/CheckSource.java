




import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckSource {

    public static final String SRC_HASH_REGEXP = ":((hg)|(git)):[a-z0-9]*\\+?";

    CheckSource(String dataFile, boolean isOpenJDK) {
        
        readFile(dataFile, isOpenJDK);
    }

    private void readFile(String fileName, boolean isOpenJDK) {
        String fishForSOURCE = null;
        String implementor = null;

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
                    continue;
                }

                
                if (readIn.startsWith("IMPLEMENTOR=")) {
                    implementor = readIn;
                    continue;
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
        }
        System.out.println("The source string found: " + fishForSOURCE);

        
        Pattern valuePattern = Pattern.compile("SOURCE=\"(.*)\"");
        Matcher valueMatcher = valuePattern.matcher(fishForSOURCE);
        if (!valueMatcher.matches()) {
            throw new RuntimeException("SOURCE string has bad format, should be SOURCE=\"<value>\"");
        }
        String valueString = valueMatcher.group(1);

        
        boolean isOracle = (implementor != null) && implementor.contains("Oracle Corporation");

        String[] values = valueString.split(" ");

        
        String rootRegexp = "\\." + SRC_HASH_REGEXP;
        if (!values[0].matches(rootRegexp)) {
            throw new RuntimeException("The test failed, first element did not match regexp: " + rootRegexp);
        }

        
        
        if (isOracle) {
            if (isOpenJDK) {
                if (values.length != 1) {
                    throw new RuntimeException("The test failed, wrong number of elements in SOURCE list." +
                            " Should be 1 for Oracle built OpenJDK.");
                }
            } else {
                if (values.length != 2) {
                    throw new RuntimeException("The test failed, wrong number of elements in SOURCE list." +
                            " Should be 2 for OracleJDK.");
                }
                
                String openRegexp = "open" + SRC_HASH_REGEXP;
                if (!values[1].matches(openRegexp)) {
                    throw new RuntimeException("The test failed, second element did not match regexp: " + openRegexp);
                }
            }
        }

        
        System.out.println("The test passed!");
    }

    public static void main(String args[]) {
        String jdkPath = System.getProperty("test.jdk");
        String runtime = System.getProperty("java.runtime.name");

        System.out.println("JDK Path : " + jdkPath);
        System.out.println("Runtime Name : " + runtime);

        new CheckSource(jdkPath + "/release", runtime.contains("OpenJDK"));
    }
}
