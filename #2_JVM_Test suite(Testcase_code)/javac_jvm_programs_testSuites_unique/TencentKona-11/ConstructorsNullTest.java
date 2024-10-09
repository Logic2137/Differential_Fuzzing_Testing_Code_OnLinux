

















import java.applet.Applet;
import java.awt.*;
import java.awt.image.*;
import java.awt.color.*;













public class ConstructorsNullTest extends Applet
 {
   

   public void init()
    {
      
      
      

      this.setLayout (new BorderLayout ());

    }

   public void start ()
    {
      
      setSize (200,200);
      show();

      ColorConvertOp gp;
      boolean passed = false;
      try {
          gp = new ColorConvertOp((ColorSpace)null, (RenderingHints)null);
      } catch (NullPointerException e) {
          try {
              gp = new ColorConvertOp((ColorSpace)null, null, null);
          } catch (NullPointerException e1) {
              try {
                  gp = new ColorConvertOp((ICC_Profile[])null, null);
              } catch (NullPointerException e2) {
                  passed = true;
              }
          }
      }

      if (!passed) {
          System.out.println("Test FAILED: one of constructors didn't throw NullPointerException.");
          throw new RuntimeException("Test FAILED: one of constructors didn't throw NullPointerException.");
      }
      System.out.println("Test PASSED: all constructors threw NullPointerException.");

      
      
      
      

    }

 }
