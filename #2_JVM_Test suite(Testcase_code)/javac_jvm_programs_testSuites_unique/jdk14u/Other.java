

package com.other;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Other {

    private static final String MSG = "other jpackage test application";
    private static final int EXPECTED_NUM_OF_PARAMS = 3; 

    public static void main(String[] args) {
        String outputFile = "appOutput.txt";
        File file = new File(outputFile);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            System.out.println(MSG);
            out.println(MSG);

            System.out.println("args.length: " + args.length);
            out.println("args.length: " + args.length);

            for (String arg : args) {
                System.out.println(arg);
                out.println(arg);
            }

            for (int index = 1; index <= EXPECTED_NUM_OF_PARAMS; index++) {
                String value = System.getProperty("param" + index);
                if (value != null) {
                    System.out.println("-Dparam" + index + "=" + value);
                    out.println("-Dparam" + index + "=" + value);
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

}
