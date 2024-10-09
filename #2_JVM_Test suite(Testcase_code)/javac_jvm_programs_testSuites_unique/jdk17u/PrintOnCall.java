
package vm.compiler.CodeCacheInfoOnCompilation;

public class PrintOnCall {

    public static void printOnCall() {
        System.out.println("PrintOnCall");
    }

    public static void main(String[] args) {
        System.out.println("before");
        printOnCall();
        System.out.println("after");
    }
}
