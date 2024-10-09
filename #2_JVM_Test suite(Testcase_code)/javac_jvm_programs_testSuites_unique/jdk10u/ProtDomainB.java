

import java.security.ProtectionDomain;







public class ProtDomainB {
  public static void main(String args[]) {
    System.out.println("Testing ProtDomainB");
    ProtectionDomain mine = ProtDomainB.class.getProtectionDomain();
    ProtectionDomain his  = ProtDomainBOther.class.getProtectionDomain();

    System.out.println("mine = " + mine);
    System.out.println("his  = " + his);

    if (mine == his) {
      System.out.println("Protection Domains match");
    } else {
      System.out.println("Protection Domains do not match!");
      System.exit(1);
    }
  }
}

class ProtDomainBOther {

}
