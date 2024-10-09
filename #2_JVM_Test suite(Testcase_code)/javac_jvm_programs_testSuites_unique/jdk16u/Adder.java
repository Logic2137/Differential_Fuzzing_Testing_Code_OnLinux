

package jdk.test;

import java.util.Arrays;

public class Adder {
    public static void main(String[] args) {
        System.out.println(Adder.class + " ...");
        System.out.println("Num args: " + args.length);
        System.out.println("args list: " + Arrays.asList(args));
        int sum = 0;
        
        
        for (String arg: args) {
            System.out.println(arg);
            if ("--".equals(arg)) {
                break;
            }
            sum += Integer.parseInt(arg);
        }
        System.exit(sum); 
    }
}
