



import sun.text.IntHashtable;

public class Bug4705389 {
   public static void main(String args[]) {
      IntHashtable table = new IntHashtable();
      for (int i = 1; i < 132; ++i) {
         table.put(i, 0);
         table.remove(i);
      }
      table.put(132, 0);
   }
}
