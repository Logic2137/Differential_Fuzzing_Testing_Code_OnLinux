



import java.io.Serializable;

public class ReorderedBoundsTest {
  public static class Parent {
    public <T extends Appendable & Serializable> String printClassName(T t) {
      return "Parent";
    }
  }

  public static class OrderedChild extends Parent {
    @Override
    public <T extends Appendable & Serializable> String printClassName(T t) {
      return "OrderedChild";
    }
  }

  public static class ReorderedChild extends Parent {
    @Override
    public <T extends Serializable & Appendable> String printClassName(T t) {
      return "ReorderedChild";
    }
  }

  public static void main(String[] args) {

    String s;
    Parent p = new OrderedChild();
    if (!(s = p.printClassName(new StringBuilder())).equals("OrderedChild"))
        throw new AssertionError("Bad output: " + s);

    p = new ReorderedChild();
    if (!(s = p.printClassName(new StringBuilder())).equals("ReorderedChild"))
        throw new AssertionError("Bad output: " + s);
  }
}
