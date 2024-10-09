

package jit.test.tr.loopReplicator;

import java.io.*;

public class Main 
   {
   public int iterations;
   public long vowCount;
   public long newlineCount;
   public static int N = 10000; 
   
   public Main() 
      {
      }
   public Main(int iters)
      {
      if (iters < 0) 
         iterations = -1*iters;
      iterations = iters;
      }
   
   public static void main(String [] argv)
      {
      if (argv.length < 2)
         {
         System.out.println("Usage: java Main file iters");
         System.out.println("Recommended setting iters = 10 [to get to scorching]");
         System.exit(1);
         }
      File f = new File(argv[0]);
      Main m = new Main(Integer.parseInt(argv[1]));
      try 
         {
         
         RandomAccessFile raf = new RandomAccessFile(f, "r");
         long remFilePointer = raf.getFilePointer(); 
         for (int i = 0; i < m.iterations; i++)
            {
            m.testProps(raf);
            raf.seek(remFilePointer); 
            }
         System.out.println("Number of vowels: "+m.vowCount);
         System.out.println("Number of newlines: "+m.newlineCount);
         }
      catch (Exception e)
         {
         
         
         System.out.println(e.toString()); 
         }
      }
   
   public void testProps(RandomAccessFile raf) throws IOException
      {
      int c;
      boolean val = true;
      long vowKeep = 32768;
      long newlineKeep = 65536;
      int counter = 0;
      
      while ((c = raf.read()) != -1 && val)
         {
         
         
         if (++counter < (0.9*N))
            {
            switch (c)
               {
               case 'e':
               case 'a':
               case 'i':
               case 'o':
               case 'u':
                       vowCount++; 
                       break;
               case '\n':
                       newlineCount++; 
                       break;
               default:
                       
                       vowKeep++;
                       newlineKeep--;
                       if (newlineKeep < 0)
                          newlineKeep = - newlineKeep;
                       break;
               }
            
            if ((vowCount%2) == 0)
               {
               vowKeep = vowKeep + vowCount;
               newlineKeep = newlineKeep + newlineCount;
               }
            else
               {
               vowKeep++;
               newlineKeep++;
               }
            }
         else
            {
            vowKeep--;
            newlineKeep--;
            if (counter >= N) 
               counter = 0;
            }
         
         if ((newlineCount%2) == 0)
            {
            newlineKeep--;
            vowKeep++;
            }
         }
      
      
      if ((vowCount%2) == 0)
         {
         vowKeep = vowCount;
         newlineKeep = newlineCount;
         val = true;
         }
      else 
         {
         vowKeep = vowKeep + vowCount;
         newlineKeep = newlineKeep + newlineCount;
         val = false;
         }
      if (val)
         printMe(false, true, vowKeep);
      else
         printMe(false, true, newlineKeep);
      }

   public static void printMe(boolean b, boolean val, long field) 
      {
      if (b)
         {
         if (val)
            System.out.print("its even ");
         else
            System.out.print("its odd ");
         System.out.println(field);
         }
      }
   }



