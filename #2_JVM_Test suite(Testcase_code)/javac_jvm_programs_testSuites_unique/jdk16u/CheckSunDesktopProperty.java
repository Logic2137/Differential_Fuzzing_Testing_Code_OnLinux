



public class CheckSunDesktopProperty {

     public static void main(String[] args) {
         String pjProp = System.getProperty("sun.desktop");
         if (pjProp != null) {
             throw new RuntimeException("pjProp = " + pjProp);
         }
     }
}
