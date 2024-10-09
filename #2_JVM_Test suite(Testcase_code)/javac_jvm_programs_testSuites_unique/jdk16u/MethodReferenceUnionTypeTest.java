



import java.util.function.Consumer;
public class MethodReferenceUnionTypeTest {
   static String blah = "NONE";
   <T> void forAll(Consumer<T> consumer, T value) { consumer.accept(value); }

   public void secondTest() {
       try {
          throwing();
        } catch (A | B ex) {
            forAll(Picture::draw, ex);
        }
   }

   void throwing() throws A, B { throw new A();}

   interface Shape { void draw(); }
   interface Marker { }
   interface Picture { void draw();  }

   class A extends Exception implements Marker, Picture { public void draw() { blah = "A"; }}
   class B extends Exception implements Marker, Picture, Shape { public void draw() {}}

   public static void main(String[] args) {
       new MethodReferenceUnionTypeTest().secondTest();
       if (!blah.equals("A"))
            throw new AssertionError("Incorrect output");
   }
}
