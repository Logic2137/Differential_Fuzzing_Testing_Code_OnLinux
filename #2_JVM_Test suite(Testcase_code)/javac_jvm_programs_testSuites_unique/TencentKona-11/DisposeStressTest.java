



















import java.applet.Applet;
import java.awt.*;













public class DisposeStressTest extends Applet
 {
   

   public void init()
    {
      
      
      

      this.setLayout (new BorderLayout ());


    }

   public void start ()
    {
        for (int i = 0; i < 1000; i++) {
            Frame f = new Frame();
            f.setBounds(10, 10, 10, 10);
            f.show();
            f.dispose();

            Frame f2 = new Frame();
            f2.setBounds(10, 10, 100, 100);
            MenuBar bar = new MenuBar();
            Menu menu = new Menu();
            menu.add(new MenuItem("foo"));
            bar.add(menu);
            f2.setMenuBar(bar);
            f2.show();
            f2.dispose();
        }
    }

 }
