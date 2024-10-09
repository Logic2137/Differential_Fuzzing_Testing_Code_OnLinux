

import java.awt.*;



public class HeadlessFileDialog {
    public static void main(String args[]) {
        FileDialog d;
        boolean exceptions = false;
        try {
            d = new FileDialog(new Frame("Hi there"));
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");

        exceptions = false;
        try {
            d = new FileDialog(new Frame("Hi there"), "Dialog title");
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");

        exceptions = false;
        try {
            d = new FileDialog(new Frame("Hi there"), "Dialog title", FileDialog.LOAD);
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");

        exceptions = false;
        try {
            d = new FileDialog(new Frame("Hi there"), "Dialog title", FileDialog.SAVE);
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
    }
}
