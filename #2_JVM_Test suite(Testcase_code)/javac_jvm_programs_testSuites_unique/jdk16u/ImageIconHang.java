

import java.awt.*;


public class ImageIconHang {
    public static void main(String[] args) throws Exception {
        Image image = Toolkit.getDefaultToolkit().getImage((String) null);
        MediaTracker mt = new MediaTracker(new Component() {});
        mt.addImage(image, 1);
        mt.waitForID(1, 5000);

        int status = mt.statusID(1, false);

        System.out.println("Status: " + status);

        if (status != MediaTracker.ERRORED) {
            throw new RuntimeException("MediaTracker.waitForID() hung.");
        }
    }
}
