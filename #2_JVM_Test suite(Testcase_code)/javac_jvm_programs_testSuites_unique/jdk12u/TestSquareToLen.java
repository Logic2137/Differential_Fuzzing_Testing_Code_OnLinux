



package compiler.intrinsics.bigInteger;

import java.math.BigInteger;
import java.util.Random;

public class TestSquareToLen {

    
    public static BigInteger base_multiply(BigInteger op1) {
      return op1.multiply(op1);
    }

    
    public static BigInteger new_multiply(BigInteger op1) {
      return op1.multiply(op1);
    }

    public static boolean bytecompare(BigInteger b1, BigInteger b2) {
      byte[] data1 = b1.toByteArray();
      byte[] data2 = b2.toByteArray();
      if (data1.length != data2.length)
        return false;
      for (int i = 0; i < data1.length; i++) {
        if (data1[i] != data2[i])
          return false;
      }
      return true;
    }

    public static String stringify(BigInteger b) {
      String strout= "";
      byte [] data = b.toByteArray();
      for (int i = 0; i < data.length; i++) {
        strout += (String.format("%02x",data[i]) + " ");
      }
      return strout;
    }

    public static void main(String args[]) throws Exception {

      BigInteger oldsum = new BigInteger("0");
      BigInteger newsum = new BigInteger("0");

      BigInteger b1, b2, oldres, newres;

      Random rand = new Random();
      long seed = System.nanoTime();
      Random rand1 = new Random();
      long seed1 = System.nanoTime();
      rand.setSeed(seed);
      rand1.setSeed(seed1);

      for (int j = 0; j < 100000; j++) {
        int rand_int = rand1.nextInt(3136)+32;
        b1 = new BigInteger(rand_int, rand);

        oldres = base_multiply(b1);
        newres = new_multiply(b1);

        oldsum = oldsum.add(oldres);
        newsum = newsum.add(newres);

        if (!bytecompare(oldres,newres)) {
          System.out.print("mismatch for:b1:" + stringify(b1) + " :oldres:" + stringify(oldres) + " :newres:" + stringify(newres));
          System.out.println(b1);
          throw new Exception("Failed");
        }
      }
      if (!bytecompare(oldsum,newsum))  {
        System.out.println("Failure: oldsum:" + stringify(oldsum) + " newsum:" + stringify(newsum));
        throw new Exception("Failed");
      } else {
        System.out.println("Success");
      }
   }
}
