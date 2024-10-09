
package compiler.aot.cli.jaotc.data;

public class HelloWorldTwo {

    public static final String MESSAGE = "HelloWorld2";

    public static void main(String[] args) {
        System.out.println(MESSAGE);
        System.out.println("Message length = " + MESSAGE.length());
    }
}
