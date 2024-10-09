

import java.awt.color.ColorSpace;
import java.util.HashMap;
import java.util.Map;


public final class GetNameTest {

    private static final Map<Integer, String[]> colorSpaces = new HashMap<>(5);

    static {
        colorSpaces.put(ColorSpace.CS_CIEXYZ, new String[] {"X", "Y", "Z"});
        colorSpaces.put(ColorSpace.CS_sRGB,
                        new String[] {"Red", "Green", "Blue"});
        colorSpaces.put(ColorSpace.CS_LINEAR_RGB,
                        new String[] {"Red", "Green", "Blue"});
        colorSpaces.put(ColorSpace.CS_GRAY, new String[] {"Gray"});
        colorSpaces.put(ColorSpace.CS_PYCC,
                        new String[] {"Unnamed color component(0)",
                                      "Unnamed color component(1)",
                                      "Unnamed color component(2)"});
    };

    public static void main(String[] args) {
        for (int csType : colorSpaces.keySet()) {
            ColorSpace cs = ColorSpace.getInstance(csType);
            String[] names = colorSpaces.get(csType);
            for (int i = 0; i < cs.getNumComponents(); i++) {
                String name = cs.getName(i);
                if (!name.equals(names[i])) {
                    System.err.println("ColorSpace with type=" + cs.getType() +
                                       " has wrong name of " + i +
                                       " component");
                    throw new RuntimeException("Wrong name of the component");
                }
            }
        }
    }
}
