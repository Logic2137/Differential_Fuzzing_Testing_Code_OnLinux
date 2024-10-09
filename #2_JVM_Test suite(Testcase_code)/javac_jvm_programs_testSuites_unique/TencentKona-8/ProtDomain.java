

import java.security.ProtectionDomain;







public class ProtDomain {
  public static void main(String args[]) {
    System.out.println("Testing ProtDomain");
    ProtectionDomain mine = ProtDomain.class.getProtectionDomain();
    ProtectionDomain his  = ProtDomainOther.class.getProtectionDomain();

    System.out.println("mine = " + mine);
    System.out.println("his  = " + his);

    if (mine == his) {
      System.out.println("Protection Domains match");
    } else {
      throw new RuntimeException("Protection Domains do not match!");
    }
  }
}

class ProtDomainOther {

}
