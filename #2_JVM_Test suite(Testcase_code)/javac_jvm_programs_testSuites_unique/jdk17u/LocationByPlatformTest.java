import java.awt.Window;

public class LocationByPlatformTest {

    public static void main(String[] args) {
        Window window = new Window(null);
        window.setSize(100, 100);
        window.setLocationByPlatform(true);
        if (!window.isLocationByPlatform()) {
            throw new RuntimeException("Location by platform is not set");
        }
        window.setLocation(10, 10);
        if (window.isLocationByPlatform()) {
            throw new RuntimeException("Location by platform is not cleared");
        }
        window.setLocationByPlatform(true);
        window.setBounds(20, 20, 50, 50);
        if (window.isLocationByPlatform()) {
            throw new RuntimeException("Location by platform is not cleared");
        }
        window.setLocationByPlatform(true);
        window.setVisible(false);
        if (window.isLocationByPlatform()) {
            throw new RuntimeException("Location by platform is not cleared");
        }
        window.setLocationByPlatform(true);
        window.setVisible(true);
        if (window.isLocationByPlatform()) {
            throw new RuntimeException("Location by platform is not cleared");
        }
        window.dispose();
    }
}
