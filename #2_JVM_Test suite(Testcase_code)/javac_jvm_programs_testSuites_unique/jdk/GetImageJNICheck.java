

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

public class GetImageJNICheck extends Component {

     public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "true");
        Toolkit tk = Toolkit.getDefaultToolkit();
        String testPath = System.getProperty("test.src", ".");
        String imgFile = testPath + java.io.File.separator + "duke.jpg";
        Image image = tk.getImage(imgFile);
        MediaTracker mt = new MediaTracker(new GetImageJNICheck() );
        mt.addImage(image, 0);
        mt.waitForAll();
        System.exit(0);
     }
}
