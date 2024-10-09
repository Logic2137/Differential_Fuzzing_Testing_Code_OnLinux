import java.util.function.Consumer;

public class MethodReferenceIntersectionInducedTest {

    static String blah;

    <T> void forAll(Consumer<T> consumer, T... values) {
        consumer.accept(values[0]);
    }

    public void secondTest() {
        forAll(Picture::draw, new MyPicture(), new Universal());
    }

    interface Shape {

        void draw();
    }

    interface Marker {
    }

    interface Picture {

        void draw();
    }

    class MyShape implements Marker, Shape {

        public void draw() {
        }
    }

    class MyPicture implements Marker, Picture {

        public void draw() {
            blah = "MyPicture";
        }
    }

    class Universal implements Marker, Picture, Shape {

        public void draw() {
            System.out.println("Universal");
        }
    }

    public static void main(String[] args) {
        new MethodReferenceIntersectionInducedTest().secondTest();
        if (!blah.equals("MyPicture"))
            throw new AssertionError("Incorrect output");
    }
}
