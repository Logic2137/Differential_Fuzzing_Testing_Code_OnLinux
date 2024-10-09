
package jit.test.tr.loopReplicator;

public class test002
   {
      public static LRprops lrp;
      public static final int limit = 18097;
      public static byte [] byteArr = new byte[limit];
      
      public test002()
         {
            
            for (int j = 0; j < byteArr.length; j++)
               {
                  int l = (j%250);
                  if (l == 0)
                     byteArr[j] = 0x0a;
                  else
                     byteArr[j] = 0x0b;
               } 
         }
      public static void main (String [] argv)
         {
            lrp = new LRprops(10, "test");
            test002 t2 = new test002();
            for (int i = 0; i < lrp.iterations; i++)
               t2.subtest002(lrp);
         }
      private boolean subtest002(LRprops lrp)
         {
            
            char [] a = new char[2*limit];
            for (int j = 0; j < lrp.iterations; j++)
               {
                  int i = 0;
                  while (i < limit)
                     {
                        if (byteArr[i] == 0x0b)
                           {
                              a[i] = (char)(byteArr[i] + 0x0c);
                              i++;
                           }
                        else 
                           {
                              int type = lrp.joker;
                              switch(type)
                                 {
                                    default:
                                       return false;
                                    case 1:
                                       i += 2;
                                       break;
                                    case 2:
                                       i += 3;
                                       break;
                                 }
                           }
                     }
               }
            if (a[limit-1] != 0x0b)
               return false;
            return true;
         }
   }
class LRprops
   {
      public LRprops(int iters, String s)
         {
            iterations = 1 << iters;
            name = s;
            joker = 1;
         }

      public String getName() { return name; }
      public int iterations;
      private String name;
      
      public int joker;
   }
