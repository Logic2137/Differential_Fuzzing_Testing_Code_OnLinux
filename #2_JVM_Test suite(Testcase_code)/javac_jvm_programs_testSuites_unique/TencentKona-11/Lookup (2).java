

import java.net.InetAddress;



public class Lookup {

    public static void main(String args[]) throws Exception {
        InetAddress addrs[] = InetAddress.getAllByName(args[0]);
        for (int i=0; i<addrs.length; i++) {
            System.out.println(addrs[i].getHostAddress());
        }
    }

}
