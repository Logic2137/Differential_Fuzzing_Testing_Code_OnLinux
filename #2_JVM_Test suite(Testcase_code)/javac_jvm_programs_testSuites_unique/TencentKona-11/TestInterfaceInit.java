


import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class TestInterfaceInit {

   static List<Class<?>> cInitOrder = new ArrayList<>();

   
   interface I {
       boolean v = TestInterfaceInit.out(I.class);
        default void ix() {}
   }

   
   interface J extends I {
       boolean v = TestInterfaceInit.out(J.class);
       default void jx() {}
   }
   
   interface JN extends J {
       boolean v = TestInterfaceInit.out(JN.class);
       public abstract void jnx();
   }

   
   interface K extends I {
       boolean v = TestInterfaceInit.out(K.class);
        default void kx() {}
   }

   
   interface KN extends K {
       boolean v = TestInterfaceInit.out(KN.class);
       static void knx() {}
   }

   interface L extends JN, KN {
       boolean v = TestInterfaceInit.out(L.class);
        default void lx() {}
   }

   static class ChildClass implements JN, KN {
       boolean v = TestInterfaceInit.out(ChildClass.class);
       public void jnx() {}
   }

   public static void main(String[] args) {
       
       boolean v = L.v;

       List<Class<?>> expectedCInitOrder = Arrays.asList(L.class);
       if (!cInitOrder.equals(expectedCInitOrder)) {
         throw new RuntimeException(String.format("Class initialization array %s not equal to expected array %s", cInitOrder, expectedCInitOrder));
       }

       ChildClass myC = new ChildClass();
       boolean w = myC.v;

       expectedCInitOrder = Arrays.asList(L.class,I.class,J.class,K.class,ChildClass.class);
       if (!cInitOrder.equals(expectedCInitOrder)) {
         throw new RuntimeException(String.format("Class initialization array %s not equal to expected array %s", cInitOrder, expectedCInitOrder));
       }

   }

   static boolean out(Class c) {
       System.out.println("#: initializing " + c.getName());
       cInitOrder.add(c);
       return true;
   }

}
