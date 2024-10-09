



import java.net.*;
import java.util.*;

public class B8035653 {
   public static void main(String[] args) throws Exception {
      try (DatagramSocket ds = new DatagramSocket();){
            ds.getLocalAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        }
   }
}
