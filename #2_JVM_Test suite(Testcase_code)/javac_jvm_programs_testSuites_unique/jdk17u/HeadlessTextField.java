import java.awt.*;

public class HeadlessTextField {

    public static void main(String[] args) {
        TextField t;
        boolean exceptions = false;
        try {
            t = new TextField();
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        exceptions = false;
        try {
            t = new TextField("Hi there");
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        try {
            t = new TextField(20);
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        exceptions = false;
        try {
            t = new TextField(200);
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        try {
            t = new TextField("Hi there", 20);
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        exceptions = false;
        try {
            t = new TextField("Hi there", 200);
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
    }
}
