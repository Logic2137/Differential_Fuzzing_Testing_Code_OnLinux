



public class CreatesInterfaceDotEqualsCallInfo {
  public static void main(String[] args) throws java.io.IOException {
    String[] jsargs = { System.getProperty("test.src", ".") +
                        "/createsInterfaceDotEqualsCallInfo.js" };
    jdk.nashorn.tools.Shell.main(System.in, System.out, System.err, jsargs);
    System.out.println("PASS, did not crash running Javascript");
  }
}
