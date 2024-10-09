



import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CompareToXrandrTest {

    public static void main(String[] args) throws Exception {
        if (!new File("/usr/bin/xrandr").exists()) {
            System.out.println("No xrandr tool to compare");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Runtime.getRuntime().exec("/usr/bin/xrandr").getInputStream()))) {
            Pattern pattern = Pattern.compile("^\\s*(\\d+x\\d+)");

            for (GraphicsDevice d : GraphicsEnvironment
                    .getLocalGraphicsEnvironment().getScreenDevices()) {

                Set<String> xrandrModes = reader.lines().map(pattern::matcher)
                        .filter(Matcher::find).map(m -> m.group(1))
                        .collect(Collectors.toSet());

                Set<String> javaModes = Arrays.stream(d.getDisplayModes())
                        .map(m -> m.getWidth() + "x" + m.getHeight())
                        .collect(Collectors.toSet());

                if (!xrandrModes.equals(javaModes)) {
                    throw new RuntimeException("Failed");
                } else {
                    System.out.println("Device " + d + ": " + javaModes.size() +
                            " modes found.");
                }
            }
        }
    }
}
