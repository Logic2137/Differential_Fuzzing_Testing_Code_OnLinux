import javax.swing.*;
import java.awt.*;

public class HeadlessJDialog {

    public static void main(String[] args) {
        boolean exceptions = false;
        JDialog b;
        try {
            b = new JDialog();
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        exceptions = false;
        try {
            b = new JDialog(new Frame("Frame title"));
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        exceptions = false;
        try {
            b = new JDialog(new Frame("Frame title"), true);
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        exceptions = false;
        try {
            b = new JDialog(new Frame("Frame title"), false);
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        exceptions = false;
        try {
            b = new JDialog(new Frame("Frame title"), "Dialog title");
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        exceptions = false;
        try {
            b = new JDialog(new Frame("Frame title"), "Dialog title", true);
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        exceptions = false;
        try {
            b = new JDialog(new Frame("Frame title"), "Dialog title", false);
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
    }
}
