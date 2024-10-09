import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.text.*;
import java.util.*;

public class AttributeValuesCastTest {

    public static void main(String[] args) {
        System.out.println("java.version = " + System.getProperty("java.version"));
        try {
            Map attributes = new HashMap();
            attributes.put("Something", "Somethign Else");
            Font f = new Font(attributes);
            System.out.println("PASS: was able to create font. ");
        } catch (Throwable t) {
            throw new RuntimeException("FAIL: caught " + t.toString());
        }
    }
}
