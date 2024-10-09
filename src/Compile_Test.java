import java.io.File;
import java.io.IOException;
import java.io.*;

public class Compile_Test {
    public static void main(String[] args) throws IOException {
        String cmd = "javac -Xlint:unchecked -encoding utf-8 -cp .:/home/Graduation_Project_Code/javac_jvm_programs_testSuites_unique/jdk8u -d ./Testcode_outClass/jdk8u /home/Graduation_Project_Code/javac_jvm_programs_testSuites_unique/jdk8u/A.java";
        // String cmd = "pwd";
        
        System.out.println("cmd命令是: " + cmd);
        
        Process process = Runtime.getRuntime().exec(cmd);

        BufferedReader errorBf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String writeLine;
        String errorLine;
        int lineIndex = 0;
        while ((errorLine = errorBf.readLine()) != null) {
                    ++lineIndex;
                    writeLine = "line" + lineIndex + ": \t" + errorLine;
                    System.out.println(writeLine);
                }
        System.out.print("\n");

        return;
    }
}
